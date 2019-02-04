package com.upgrade.campsite.mycamp.controllers;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.model.dtos.PeriodReservationDto;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservation-periods")
public class ReservationPeriodAvailableController {

    @Autowired
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @GetMapping
    public List<PeriodReservationDto> findPeriodsAvailable(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate) throws BusinessException {
        return reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(startDate, finalDate);
    }

    @PostMapping
    public ResponseEntity createPeriodReservation(@RequestBody ReservationPeriodAvailable reservationPeriodAvailable) {
        return ResponseEntity.ok(reservationPeriodAvailableService.save(reservationPeriodAvailable));
    }

}
