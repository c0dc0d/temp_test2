package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation-period")
public class ReservationPeriodAvailableController {

    @Autowired
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @GetMapping
    public ResponseEntity findAllPertiodAvailable() {
        return ResponseEntity.ok(reservationPeriodAvailableService.findAll());
    }

    @PostMapping
    public ResponseEntity createPeriodReservation(@RequestBody ReservationPeriodAvailable reservationPeriodAvailable) {
        return ResponseEntity.ok(reservationPeriodAvailableService.save(reservationPeriodAvailable));
    }

}
