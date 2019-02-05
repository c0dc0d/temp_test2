package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.model.dtos.PeriodReservationDto;
import com.upgrade.campsite.mycamp.repository.ReservationPeriodAvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationPeriodAvailableService {

    private static final String STATUS_AVAILABLE = "Y";

    @Autowired
    private ReservationPeriodAvailableRepository reservationPeriodAvailableRepository;

    @Autowired
    private ReservationService reservationService;

    @Transactional
    public ReservationPeriodAvailable save(ReservationPeriodAvailable reservationPeriodAvailable){
        return reservationPeriodAvailableRepository.save(reservationPeriodAvailable);
    }

    public ReservationPeriodAvailable findAvailablePeriod(LocalDate arrivalDate, LocalDate departureDate) {
        return reservationPeriodAvailableRepository.findAvailablePeriod(arrivalDate, departureDate);
    }

    public List<PeriodReservationDto> findPeriodsAvailableByRangeDate(LocalDate startDate, LocalDate finalDate) throws BusinessException {
        startDate = emptyStartDateHandling(startDate);
        finalDate = emptyFinalDateHandling(startDate, finalDate);
        checkRangeInsideAvailablePeriodDate(startDate, finalDate);
        List<Reservation> reservationsByNumberOfReservation = reservationService.findReservationsAccepted();
        List<PeriodReservationDto> freePeriods = new ArrayList<>();
        int quantityPeriods = reservationsByNumberOfReservation.size();
        if(quantityPeriods == 0 ){
            freePeriods.add(PeriodReservationDto
                .builder()
                .beginDate(startDate)
                .finalDate(finalDate)
                .build());
            return freePeriods;
        }
        for (int i = 0; i < quantityPeriods; i++) {
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

        Reservation lastReservation = reservationsByNumberOfReservation.get(quantityPeriods - 1);
        if (!finalDate.isEqual(lastReservation.getDepartureDate()) && !isBetween(finalDate, lastReservation.getArrivalDate(), lastReservation.getDepartureDate())) {
            freePeriods.add(PeriodReservationDto.builder()
                    .beginDate(lastReservation.getDepartureDate())
                    .finalDate(finalDate).build());
        }

        return freePeriods;
    }

    private void checkRangeInsideAvailablePeriodDate(LocalDate startDate, LocalDate finalDate) throws BusinessException {
        ReservationPeriodAvailable rpa = reservationPeriodAvailableRepository.findByStatusAvailable(STATUS_AVAILABLE);
        if(rpa == null) {
            throw new BusinessException("There aren't any periods available");
        }
        if(!(startDate.equals(rpa.getFirstDateAvailable()) || startDate.isAfter(rpa.getFirstDateAvailable())) &&
                (finalDate.equals(rpa.getFinalDateAvailable()) || startDate.isBefore(rpa.getFirstDateAvailable()))) {
            throw new BusinessException("The range of dates isn't contained in the available period");
        }
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
