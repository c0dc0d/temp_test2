package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.constants.BusinessException;
import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.jms.ReservationReceiver;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.repository.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.UUID;

@Service
public class ReservationService {

    private static final String JMSX_GROUP_ID = "JMSXGroupID";
    private static final String MY_CAMP_QUEUE_GROUP_NAME = "MyCampGroupQueue";

    @Autowired
    private ReservationsRepository reservationsRepository;

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
    public Reservation sendProcessing(Reservation reservation) {
        Reservation reservationSaved = createReservationPending(reservation);
        jmsTemplate.send(ReservationReceiver.RESERVATION_QUEUE_NAME, session -> {
            ObjectMessage om = session.createObjectMessage();
            om.setObject(reservationSaved);
            om.setStringProperty(JMSX_GROUP_ID, MY_CAMP_QUEUE_GROUP_NAME);
            return om;
        });
        return reservationSaved;
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
            throw new BusinessException("The reservation wasn't found: " + numberOfReservation);
        }
        return rsvByNumberOfReservation;
    }

}