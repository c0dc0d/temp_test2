package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.repository.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping
    public void createReservation(@RequestBody Reservation reservation) {
        jmsTemplate.convertAndSend("ReservationQueue", reservation);
    }

}
