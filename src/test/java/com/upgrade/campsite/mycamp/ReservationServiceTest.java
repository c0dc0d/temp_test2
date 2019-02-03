package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.ReservationsStatusDto;
import com.upgrade.campsite.mycamp.repository.ReservationRepository;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import com.upgrade.campsite.mycamp.service.ReservationService;
import com.upgrade.campsite.mycamp.utils.UtilTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @Mock
    private JmsTemplate jmsTemplate;

    @Before
    public void setUp() {
        when(reservationPeriodAvailableService
                .findAvailablePeriod(any(), any())).thenReturn(UtilTest.getPeriodAvailable());
    }

    @Test
    public void Should_ThrowNotFoundPeriodReservation_When_ThereInstPeriodAvailable() {
        try {
            reservationService.sendProcessing(UtilTest.getReservationWithJulyThreeDays());
        } catch (BusinessException e) {
            assertEquals("The period of reservation isn't available", e.getMessage());
        }
    }

    @Test
    public void Should_ThrowLimitOfPermanenceException_When_DaysOfPermanenceIsMoreThanThreeDays() {
        try {
            reservationService.sendProcessing(UtilTest.getReservationWithMoreThanThreeDays());
        } catch (BusinessException e) {
            assertEquals("The reservations exceeded the limit of permanence", e.getMessage());
        }
    }

    @Test
    public void Should_ThrowMinorLimitOfDoReservation_When_TryDoReservationsSameDaysOfArrival() {
        try {
            reservationService.sendProcessing(UtilTest.getReservationArrivalDateSameFirstDatePeriodAvailable());
        } catch (BusinessException e) {
            assertEquals("The reservations exceeded the minor limit (minor limit: 1 day ahead of arrival) to make reservations",
                    e.getMessage());
        }
    }

    @Test
    public void Should_ThrowMajorLimitOfDoReservation_When_TryDoReservationsMoreThanThirtyDays() {
        try {
            reservationService.sendProcessing(UtilTest.getReservationThreeDaysInMay());
        } catch (BusinessException e) {
            assertEquals("The reservations exceeded the major limit (major limit: 30 days in advance) to make reservations",
                    e.getMessage());
        }
    }

    @Test
    public void Should_CreateReservation_When_ArrivalDateAndDepartureDateIsAvailable() throws BusinessException {
        Reservation reservation = reservationService.sendProcessing(UtilTest.getReservationWithJulyThreeDays());
        assertNotNull(reservation);
        assertNotNull(reservation.getNumberOfReservation());
        assertEquals(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION, reservation.getStatusReservation());
    }

    @Test
    public void Should_ThrowNotFoundReservation_When_ChangeReservationNotExist() {
        String numberOfReservation = "123zxc";
        LocalDate newArrivalDate = LocalDate.of(2019, 7, 8);
        LocalDate newDepartureDate = LocalDate.of(2019, 7, 11);

        try {
            reservationService.changeReservation(
                    numberOfReservation,
                    newArrivalDate,
                    newDepartureDate);
        } catch (BusinessException e) {
            assertEquals("The reservation wasn't found: "+numberOfReservation, e.getMessage());
        }
    }

    @Test
    public void Should_ChangeReservationWithNewDatesAndPending_When_ChangeReservation() throws BusinessException {
        String numberOfReservation = "123zxc";
        LocalDate newArrivalDate = LocalDate.of(2019, 7, 8);
        LocalDate newDepartureDate = LocalDate.of(2019, 7, 11);
        Reservation reservationWithNumberOfReservation = UtilTest.getReservationWithNumberOfReservation();
        when(reservationRepository.findByNumberOfReservation(any()))
                .thenReturn(reservationWithNumberOfReservation);
        Reservation reservation = reservationService
                .changeReservation(numberOfReservation, newArrivalDate, newDepartureDate);
        assertEquals(reservation.getNumberOfReservation(), numberOfReservation);
        assertEquals(reservation.getArrivalDate(), newArrivalDate);
        assertEquals(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION, reservation.getStatusReservation());
    }

    @Test
    public void Should_ThrowNotFoundReservation_When_GivenNumberOfReservationNotExist() {
        String numberOfReservation = "123zxc";
        try {
            reservationService.cancelReservation(numberOfReservation);
        } catch (BusinessException e) {
            assertEquals("The reservation wasn't found: "+numberOfReservation, e.getMessage());
        }
    }

    @Test
    public void Should_CancelReservation_When_GivenNumberOfReservation() throws BusinessException {
        Reservation reservationWithNumberOfReservation = UtilTest.getReservationWithNumberOfReservation();
        when(reservationRepository.findByNumberOfReservation(any()))
                .thenReturn(reservationWithNumberOfReservation);
        Reservation reservation =
                reservationService.cancelReservation(reservationWithNumberOfReservation.getNumberOfReservation());
        assertEquals(StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION, reservation.getStatusReservation());
    }

    @Test
    public void Should_ThrowNotFoundReservation_When_GivenNumberOfReservationToSearchStatusAcceptanceNotExist() {
        String numberOfReservation = "123zxc";
        try {
            reservationService.findStatusReservationAcceptance(numberOfReservation);
        } catch (BusinessException e) {
            assertEquals("The reservation wasn't found: "+numberOfReservation, e.getMessage());
        }
    }

    @Test
    public void Should_GetReservation_When_GivenNumberOfReservationToSearchStatusAccept() throws BusinessException {
        Reservation reservationWithNumberOfReservation = UtilTest.getReservationWithNumberOfReservation();
        when(reservationRepository.findByNumberOfReservation(any()))
                .thenReturn(reservationWithNumberOfReservation);
        ReservationsStatusDto statusReservationAcceptance =
                reservationService.findStatusReservationAcceptance(reservationWithNumberOfReservation.getNumberOfReservation());
        assertTrue(statusReservationAcceptance.getReservationAcceptance());
    }
}
