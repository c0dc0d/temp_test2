package com.upgrade.campsite.mycamp.jms;

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
            reservationService.turnReservationsDenied(reservation);
        }else {
            reservationService.turnReservationConfirmed(reservation);
        }
    }

}
