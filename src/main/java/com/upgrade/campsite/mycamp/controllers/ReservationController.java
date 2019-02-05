package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservation(@RequestBody Reservation reservation) throws BusinessException {
        return ResponseEntity
                .accepted()
                .body(reservationService.sendProcessing(reservation));
    }

    @PutMapping("/{numberOfReservation}/cancellations")
    public ResponseEntity cancelReservation(@PathVariable String numberOfReservation) throws BusinessException {
        return ResponseEntity.ok(reservationService.cancelReservation(numberOfReservation));
    }

    @GetMapping("{numberOfReservation}")
    public ResponseEntity findReservation(@PathVariable String numberOfReservation) {
        return ResponseEntity.ok(reservationService.findByNumberOfReservation(numberOfReservation));
    }

    @GetMapping("{numberOfReservation}/status")
    public ResponseEntity findStatusReservationAcceptance(@PathVariable String numberOfReservation) throws BusinessException {
        return ResponseEntity.ok(reservationService.findStatusReservationAcceptance(numberOfReservation));
    }

    @PutMapping("{numberOfReservation}/alterations")
    public ResponseEntity modidyReservation(@PathVariable String numberOfReservation,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate) throws BusinessException {
        return ResponseEntity.accepted().body(reservationService.changeReservation(numberOfReservation, startDate, finalDate));
    }

}
