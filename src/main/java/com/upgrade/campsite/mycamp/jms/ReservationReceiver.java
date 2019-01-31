package com.upgrade.campsite.mycamp.jms;

import com.upgrade.campsite.mycamp.repository.Reservation;
import com.upgrade.campsite.mycamp.repository.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationReceiver {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @JmsListener(destination = "ReservationQueue")
    public void receiver(Reservation reservation) {
        reservationsRepository.save(reservation);
    }

}
