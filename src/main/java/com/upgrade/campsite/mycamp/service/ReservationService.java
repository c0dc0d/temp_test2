package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.jms.ReservationReceiver;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.model.ReservationsStatusDto;
import com.upgrade.campsite.mycamp.model.User;
import com.upgrade.campsite.mycamp.repository.ReservationPeriodAvailableRepository;
import com.upgrade.campsite.mycamp.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.jms.*;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private static final String JMSX_GROUP_ID = "JMSXGroupID";
    private static final String MY_CAMP_QUEUE_GROUP_NAME = "MyCampGroupQueue";
    private static final int LIMIT_OF_DATE_FOR_RESERVATIONS = 3;
    private static final int MINOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS = 2;
    private static final int MAJOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS = 30;
    private static final String THE_RESERVATION_WASNT_FOUND = "The reservation wasn't found: %s";

    @Autowired
    private ReservationRepository reservationsRepository;

    @Autowired
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @Autowired
    private JmsTemplate jmsTemplate;

    public Reservation findByNumberOfReservation(String numberOfReservation) {
        return reservationsRepository.findByNumberOfReservation(numberOfReservation);
    }

    public Reservation createReservationPending(Reservation reservation) {
        reservation.setNumberOfReservation(UUID.randomUUID().toString());
        reservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION);
        return reservationsRepository.save(reservation);
    }

    @Transactional
    public Reservation sendProcessing(Reservation reservation) throws BusinessException {
        validationsReservations(reservation.getArrivalDate(), reservation.getDepartureDate());
        Reservation reservationSaved = createReservationPending(reservation);
        sendingQueueProcessingMessage(reservationSaved);
        return reservationSaved;
    }

    @Transactional
    public Reservation changeReservation(String numberOfReservation, LocalDate arrivalDate, LocalDate departureDate) throws BusinessException {
        Reservation reservation = reservationsRepository.findByNumberOfReservation(numberOfReservation);
        if(reservation == null) {
            throw new BusinessException(String.format(THE_RESERVATION_WASNT_FOUND, numberOfReservation));
        }
        validationsReservations(arrivalDate, departureDate);
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        reservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION);
        sendingQueueProcessingMessage(reservation);
        return reservation;
    }

    private void sendingQueueProcessingMessage(Reservation reservation) {
        jmsTemplate.send(ReservationReceiver.RESERVATION_QUEUE_NAME, session -> {
            ObjectMessage om = session.createObjectMessage();
            om.setObject(reservation);
            om.setStringProperty(JMSX_GROUP_ID, MY_CAMP_QUEUE_GROUP_NAME);
            return om;
        });
    }

    public Integer existsReservation(Reservation reservation) {
        return reservationsRepository.existsReservation(reservation.getArrivalDate(), reservation.getDepartureDate());
    }

    @Transactional
    public void changeReservationsStatus(Reservation reservation, String codeStatusOfReservation) {
        if(reservation.getId() != null) {
            reservation.setStatusReservation(codeStatusOfReservation);
            reservationsRepository.save(reservation);
        }
    }

    @Transactional
    public Reservation cancelReservation(String numberOfReservation) throws BusinessException {
        Reservation rsvByNumberOfReservation = reservationsRepository.findByNumberOfReservation(numberOfReservation);
        if(rsvByNumberOfReservation != null) {
            rsvByNumberOfReservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION);
        }else {
            throw new BusinessException(String.format(THE_RESERVATION_WASNT_FOUND,numberOfReservation));
        }
        return rsvByNumberOfReservation;
    }

    public ReservationsStatusDto findStatusReservationAcceptance(String numberOfReservation) {
        Reservation rscByNumberOfReservation = reservationsRepository.findByNumberOfReservation(numberOfReservation);
        if(rscByNumberOfReservation == null) {
            return ReservationsStatusDto
                .builder()
                .numberOfReservation(numberOfReservation)
                .reservationAcceptance(Boolean.FALSE).build();
        }
        return ReservationsStatusDto
                .builder()
                .numberOfReservation(rscByNumberOfReservation.getNumberOfReservation())
                .reservationAcceptance(
                        rscByNumberOfReservation
                                .getStatusReservation().equals(StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION) ?
                                Boolean.TRUE : Boolean.FALSE).build();
    }

    private void validationOfValidRageDateOfCamp(LocalDate arrivalDate, LocalDate departureDate) throws BusinessException {
        long days = Period.between(arrivalDate ,departureDate).getDays();
        if(days > LIMIT_OF_DATE_FOR_RESERVATIONS) {
            throw new BusinessException("The reservations exceeded the limit of permanence");
        }
    }

    private void validationOfReservationCanDone(LocalDate arrivalDate) throws BusinessException {
        long days = Period.between(LocalDate.now(), arrivalDate).getDays();
        if(days < MINOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS) {
            throw new BusinessException(
                    String.format("The reservations exceeded the minor limit (minor limit: %d day ahead of arrival) to make reservations",
                            MINOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS - 1));
        }
        if(days > MAJOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS) {
            throw new BusinessException(
                    String.format("The reservations exceeded the major limit (major limit: %d days in advance) to make reservations",
                            MAJOR_LIMIT_OF_DAYS_TO_MAKE_RESERVATIONS));
        }
    }

    private void validationsReservations(LocalDate arrivalDate, LocalDate departureDate) throws BusinessException {
        validationDatePeriodReservationAvailable(arrivalDate, departureDate);
        validationOfValidRageDateOfCamp(arrivalDate, departureDate);
        validationOfReservationCanDone(arrivalDate);
    }

    private void validationDatePeriodReservationAvailable(LocalDate arrivalDate, LocalDate departureDate) throws BusinessException {
        List<ReservationPeriodAvailable> availablePeriod = reservationPeriodAvailableService.findAvailablePeriod(arrivalDate, departureDate);
        if(CollectionUtils.isEmpty(availablePeriod)) {
            throw new BusinessException("The period of reservation isn't available");
        }
    }

}