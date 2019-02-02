package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.constants.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity
                .accepted()
                .body(reservationService.sendProcessing(reservation));
    }

    @PutMapping("/cancellations/{numberOfReservation}")
    public ResponseEntity cancelReservation(@PathVariable String numberOfReservation) throws BusinessException {
        return ResponseEntity.ok(reservationService.cancelReservation(numberOfReservation));
    }

    @GetMapping("{numberOfReservation}")
    public ResponseEntity findReservation(@PathVariable String numberOfReservation) {
        return ResponseEntity.ok(reservationService.findByNumberOfReservation(numberOfReservation));
    }

}
