package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED, reason = "The reservation was sent to queue to processing")
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationService.sendProcessing(reservation);
    }

}
