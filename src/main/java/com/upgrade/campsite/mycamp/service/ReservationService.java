package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.jms.ReservationReceiver;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.repository.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    public Reservation createReservationPending(Reservation reservation) {
        reservation.setVersion(UUID.randomUUID());
        reservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION);
        return reservationsRepository.save(reservation);
    }

    @Transactional
    public Reservation sendProcessing(Reservation reservation) {
        Reservation reservationSaved = createReservationPending(reservation);
        jmsTemplate.convertAndSend(ReservationReceiver.RESERVATION_QUEUE_NAME, reservationSaved);
        return reservationSaved;
    }

    public Integer existsReservation(Reservation reservation) {
        return reservationsRepository.existsReservation(reservation.getArrivalDate(), reservation.getDepartureDate());
    }

    @Transactional
    public void turnReservationsDenied(Reservation reservation) {
        if(reservation.getId() != null) {
            reservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_DENIED_RESERVATION);
            reservationsRepository.save(reservation);
        }
    }

    @Transactional
    public void turnReservationConfirmed(Reservation reservation) {
        if(reservation.getId() != null) {
            reservation.setStatusReservation(StatusCodeReservation.CODE_STATUS_CONFIRMED_RESERVATION);
            reservationsRepository.save(reservation);
        }
    }

}
