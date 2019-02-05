package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.dtos.PeriodReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationPeriodAvailableService {

    @Autowired
    private ReservationService reservationService;

    public List<PeriodReservationDto> findPeriodsAvailableByRangeDate(LocalDate startDate, LocalDate finalDate) throws BusinessException {
        startDate = emptyStartDateHandling(startDate);
        finalDate = emptyFinalDateHandling(startDate, finalDate);
        List<Reservation> reservationsByNumberOfReservation =
                reservationService
                        .findByDateRangeAndStatusReservation(StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION,
                        startDate, finalDate);
        List<PeriodReservationDto> freePeriods = new ArrayList<>();

        if(reservationsByNumberOfReservation.isEmpty()){
            freePeriods.add(PeriodReservationDto
                .builder()
                .beginDate(startDate)
                .finalDate(finalDate)
                .build());
            return freePeriods;
        }
        int reservationQuantity = reservationsByNumberOfReservation.size();
        for (int i = 0; i < reservationQuantity; i++) {
            Reservation reservation = reservationsByNumberOfReservation.get(i);
            boolean isFirst = i == 0;
            if (isFirst) {
                if (!startDate.isEqual(reservation.getArrivalDate()) && !isBetween(startDate, reservation.getArrivalDate(), reservation.getDepartureDate())) {
                    freePeriods.add(PeriodReservationDto.builder()
                        .beginDate(startDate)
                        .finalDate(reservation.getArrivalDate()).build());
                }
            } else {
                Reservation reservationBefore = reservationsByNumberOfReservation.get(i - 1);
                PeriodReservationDto pReservation = PeriodReservationDto.builder()
                        .beginDate(reservationBefore.getDepartureDate())
                        .finalDate(reservation.getArrivalDate()).build();
                if (Math.abs(ChronoUnit.DAYS.between(pReservation.getBeginDate(), pReservation.getFinalDate())) >= 1) {
                    freePeriods.add(pReservation);
                }
            }
        }

        Reservation lastReservation = reservationsByNumberOfReservation.get(reservationQuantity - 1);
        if (!finalDate.isEqual(lastReservation.getArrivalDate()) && !isBetween(finalDate, lastReservation.getArrivalDate(), lastReservation.getDepartureDate())) {
            freePeriods.add(PeriodReservationDto.builder()
                    .beginDate(lastReservation.getDepartureDate())
                    .finalDate(finalDate).build());
        }

        return freePeriods;
    }

    private LocalDate emptyStartDateHandling(LocalDate startDate) {
        if(startDate == null) {
            return  LocalDate.now();
        }
        return startDate;
    }

    private LocalDate emptyFinalDateHandling(LocalDate startDate, LocalDate finalDate) {
        if(finalDate == null) {
            return  startDate.plusDays(30);
        }
        return finalDate;
    }

    private boolean isBetween(LocalDate day, LocalDate startDate, LocalDate finalDate) {
        return day.isAfter(startDate) && day.isBefore(finalDate);
    }
}
