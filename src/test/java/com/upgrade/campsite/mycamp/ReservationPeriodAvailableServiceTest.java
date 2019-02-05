package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.model.dtos.PeriodReservationDto;
import com.upgrade.campsite.mycamp.repository.ReservationPeriodAvailableRepository;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import com.upgrade.campsite.mycamp.service.ReservationService;
import com.upgrade.campsite.mycamp.utils.UtilTest;
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
    private ReservationPeriodAvailableRepository reservationPeriodAvailableRepository;

    @Mock
    private ReservationService reservationService;

    @Test
    public void Should_ThrowNoPeriodAvailableWasFound_When_NoPeriodAvailable() {
        try {
            reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(LocalDate.now(), LocalDate.now());
        } catch (BusinessException e) {
            assertEquals("There aren't any periods available", e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_ThrowGivenDateOutOfPeriodAvailable_When_RangeOfDatesOutOfPeriodAvailable() {
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(UtilTest.getReservationPeriodAvailable());
        try {
            reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(LocalDate.now(), LocalDate.now());
        } catch (BusinessException e) {
            assertEquals("The range of dates isn't contained in the available period", e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_GivenOneFullPeriodAvailableWithThirtyDays_When_NoReservationsWasDoneAndNoRageNoDateParametrized() throws BusinessException {
        ReservationPeriodAvailable reservationPeriodAvailable =
                ReservationPeriodAvailable.builder()
                        .firstDateAvailable(LocalDate.now())
                        .finalDateAvailable(LocalDate.now().plusDays(30))
                        .build();
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(reservationPeriodAvailable);
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(null, null);
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
        assertEquals(reservationPeriodAvailable.getFirstDateAvailable(),periodsAvailableByRangeDate.get(0).getBeginDate());
        assertEquals(reservationPeriodAvailable.getFinalDateAvailable(),periodsAvailableByRangeDate.get(0).getFinalDate());
        assertEquals(THIRTY_DAYS,
                Math.abs(ChronoUnit.DAYS.between(periodsAvailableByRangeDate.get(0).getBeginDate(),
                        periodsAvailableByRangeDate.get(0).getFinalDate())));
    }

    @Test
    public void Should_GivenOneFullPeriodAvailable_When_NoReservationsWasDone() throws BusinessException {
        ReservationPeriodAvailable reservationPeriodAvailable = UtilTest.getReservationPeriodAvailable();
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(reservationPeriodAvailable);
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.of(2019,7,1), LocalDate.of(2019,7,30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
        assertEquals(reservationPeriodAvailable.getFirstDateAvailable(),periodsAvailableByRangeDate.get(0).getBeginDate());
        assertEquals(reservationPeriodAvailable.getFinalDateAvailable(),periodsAvailableByRangeDate.get(0).getFinalDate());
    }

    @Test
    public void Should_GivenTowPeriodAvailable_When_OneReservationsWasDone() throws BusinessException {
        ReservationPeriodAvailable reservationPeriodAvailable = UtilTest.getReservationPeriodAvailable();
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(reservationPeriodAvailable);
        when(reservationService.findReservationsAccepted())
                .thenReturn(Collections.singletonList(UtilTest.getReservationWithNumberOfReservation()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.of(2019,7,1), LocalDate.of(2019,7,30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 2);
        assertEquals(periodsAvailableByRangeDate.get(0).getBeginDate(), LocalDate.of(2019,7,1));
        assertEquals(periodsAvailableByRangeDate.get(0).getFinalDate(), LocalDate.of(2019,7,3));
        assertEquals(periodsAvailableByRangeDate.get(1).getBeginDate(), LocalDate.of(2019,7,6));
        assertEquals(periodsAvailableByRangeDate.get(1).getFinalDate(), LocalDate.of(2019,7,30));
    }


    @Test
    public void Should_GivenThreePeriodAvailable_When_TwoReservationsWasDone() throws BusinessException {
        ReservationPeriodAvailable reservationPeriodAvailable = UtilTest.getReservationPeriodAvailable();
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(reservationPeriodAvailable);
        when(reservationService.findReservationsAccepted())
                .thenReturn(Arrays.asList(UtilTest.getReservationFromThreeToSixJuly(),
                                          UtilTest.getReservationFromEightToElevenJuly()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.of(2019,7,1), LocalDate.of(2019,7,30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 3);
        assertEquals(periodsAvailableByRangeDate.get(0).getBeginDate(), LocalDate.of(2019,7,1));
        assertEquals(periodsAvailableByRangeDate.get(0).getFinalDate(), LocalDate.of(2019,7,3));
        assertEquals(periodsAvailableByRangeDate.get(1).getBeginDate(), LocalDate.of(2019,7,6));
        assertEquals(periodsAvailableByRangeDate.get(1).getFinalDate(), LocalDate.of(2019,7,8));
        assertEquals(periodsAvailableByRangeDate.get(2).getBeginDate(), LocalDate.of(2019,7,11));
        assertEquals(periodsAvailableByRangeDate.get(2).getFinalDate(), LocalDate.of(2019,7,30));
    }

    @Test
    public void Should_GivenOnePeriodAvailable_When_TwoLimitsPeriodsReservationsWasDone() throws BusinessException {
        ReservationPeriodAvailable reservationPeriodAvailable = UtilTest.getReservationPeriodAvailable();
        when(reservationPeriodAvailableRepository
                .findByStatusAvailable(any())).thenReturn(reservationPeriodAvailable);
        when(reservationService.findReservationsAccepted())
                .thenReturn(Arrays.asList(UtilTest.getReservationFromThirtyJuneToTwoJuly(),
                                          UtilTest.getReservationFromThirtyJulyToOneAugust()));
        List<PeriodReservationDto> periodsAvailableByRangeDate =
                reservationPeriodAvailableService.findPeriodsAvailableByRangeDate(
                        LocalDate.of(2019,7,1), LocalDate.of(2019,7,30));
        assertFalse(CollectionUtils.isEmpty(periodsAvailableByRangeDate));
        assertEquals(periodsAvailableByRangeDate.size(), 1);
        assertEquals(periodsAvailableByRangeDate.get(0).getBeginDate(), LocalDate.of(2019,7,2));
        assertEquals(periodsAvailableByRangeDate.get(0).getFinalDate(), LocalDate.of(2019,7,29));
    }

}
