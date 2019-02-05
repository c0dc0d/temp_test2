package com.upgrade.campsite.mycamp.jms;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationReceiver {

    public static final String RESERVATION_QUEUE_NAME = "ReservationQueue";

    @Autowired
    private ReservationService reservationService;

    @JmsListener(destination = RESERVATION_QUEUE_NAME)
    public void receiver(Reservation reservation) {
        Integer numberOfReservations = reservationService.existsReservation(reservation);
        if(numberOfReservations != null && numberOfReservations > 0) {
            reservationService.changeReservationsStatus(reservation, StatusCodeReservation.CODE_STATUS_DENIED_RESERVATION);
        }else {
            if(reservation.getNumberOfOldReservation() != null) {
                Reservation oldReservation = reservationService.findByNumberOfReservation(reservation.getNumberOfOldReservation());
                reservationService.changeReservationsStatus(oldReservation, StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION);
            }
            reservationService.changeReservationsStatus(reservation, StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION);
        }
    }
}
