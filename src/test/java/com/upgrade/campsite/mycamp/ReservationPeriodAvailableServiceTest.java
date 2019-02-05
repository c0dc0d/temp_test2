package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.dtos.PeriodReservationDto;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationPeriodAvailableServiceTest {

    private static final int THIRTY_DAYS = 30;

    @InjectMocks
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @Mock
    private ReservationService reservationService;

    @Test
    public void Should_GivenOneFullPeriodAvailableWithThirtyDays_When_NoReservationsWasDoneAndNoRageNoDateParametrized() throws BusinessException {
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(null, null);
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
        assertEquals(THIRTY_DAYS,
                Math.abs(ChronoUnit.DAYS.between(periodsAvailableByRangeDate.get(0).getBeginDate(),
                        periodsAvailableByRangeDate.get(0).getFinalDate())));
    }

    @Test
    public void Should_GivenOneFullPeriodAvailable_When_NoReservationsWasDone() throws BusinessException {
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.of(2019,7,1), LocalDate.of(2019,7,30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
    }

    @Test
    public void Should_GivenTowPeriodAvailable_When_OneReservationsWasDone() throws BusinessException {
        when(reservationService.findByDateRangeAndStatusReservation(any(), any(), any()))
                .thenReturn(Collections.singletonList(Reservation.builder()
                    .arrivalDate(LocalDate.now().plusDays(2))
                    .departureDate(LocalDate.now().plusDays(3)).build()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.now(), LocalDate.now().plusDays(30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 2);
    }


    @Test
    public void Should_GivenThreePeriodAvailable_When_TwoReservationsWasDone() throws BusinessException {
        when(reservationService.findByDateRangeAndStatusReservation(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        Reservation.builder()
                            .arrivalDate(LocalDate.now().plusDays(2))
                            .departureDate(LocalDate.now().plusDays(3)).build(),
                        Reservation.builder()
                                .arrivalDate(LocalDate.now().plusDays(12))
                                .departureDate(LocalDate.now().plusDays(13)).build()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.now(), LocalDate.now().plusDays(30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 3);
    }

    @Test
    public void Should_GivenOnePeriodAvailable_When_TwoLimitsPeriodsReservationsWasDone() throws BusinessException {
        when(reservationService.findByDateRangeAndStatusReservation(any(), any(), any()))
                .thenReturn(Arrays.asList(
                        Reservation.builder()
                                .arrivalDate(LocalDate.now().minusDays(1))
                                .departureDate(LocalDate.now().plusDays(2)).build(),
                        Reservation.builder()
                                .arrivalDate(LocalDate.now().plusDays(30))
                                .departureDate(LocalDate.now().plusDays(32)).build()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.now(), LocalDate.now().plusDays(30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
    }
}
