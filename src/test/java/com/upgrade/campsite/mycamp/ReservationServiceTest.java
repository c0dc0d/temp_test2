package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.dtos.ReservationsStatusDto;
import com.upgrade.campsite.mycamp.repository.ReservationRepository;
import com.upgrade.campsite.mycamp.service.ReservationService;
import com.upgrade.campsite.mycamp.utils.UtilTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @Test
    public void Should_ThrowLimitOfPermanenceException_When_DaysOfPermanenceIsMoreThanThreeDays() {
        try {
            reservationService.sendProcessing(UtilTest.getReservationWithMoreThanThreeDays());
        } catch (BusinessException e) {
            assertEquals("The reservation exceeded the limit of permanence", e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_ThrowMinimumLimitOfDoReservation_When_TryDoReservationsSameDaysOfArrival() {
        try {
            reservationService.sendProcessing(Reservation.builder()
                            .arrivalDate(LocalDate.now())
                            .departureDate(LocalDate.now().plusDays(2)).build());
        } catch (BusinessException e) {
            assertEquals("The reservation exceeded the minimum limit (minimum limit: 1 day ahead of arrival) to make reservations",
                    e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_ThrowMaximumLimitOfDoReservation_When_TryDoReservationsMoreThanThirtyDays() {
        try {
            reservationService.sendProcessing(Reservation.builder()
                    .arrivalDate(LocalDate.now().minusDays(35))
                    .departureDate(LocalDate.now().minusDays(37)).build());
        } catch (BusinessException e) {
            assertEquals("The reservation exceeded the maximum limit (maximum limit: 30 days in advance) to make reservations",
                    e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_CreateReservation_When_ArrivalDateAndDepartureDateIsAvailable() throws BusinessException {
        Reservation reservation = reservationService.sendProcessing(
                Reservation.builder()
                        .arrivalDate(LocalDate.now().plusDays(1))
                        .departureDate(LocalDate.now().plusDays(3)).build()
        );
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
            return;
        }
        fail("Didn't throw the exception expected.");
    }

    @Test
    public void Should_ChangeReservationWithNewDatesAndPending_When_ChangeReservation() throws BusinessException {
        String numberOfReservation = "123zxc";
        LocalDate newArrivalDate = LocalDate.now().plusDays(5);
        LocalDate newDepartureDate = LocalDate.now().plusDays(7);
        Reservation reservationWithNumberOfReservation =
                Reservation.builder()
                    .arrivalDate(LocalDate.now().plusDays(1))
                    .departureDate(LocalDate.now().plusDays(2)).build();
        when(reservationRepository.findByNumberOfReservation(any()))
                .thenReturn(reservationWithNumberOfReservation);
        Reservation newReservation = reservationService
                .changeReservation(numberOfReservation, newArrivalDate, newDepartureDate);
        assertFalse(numberOfReservation.equals(newReservation.getNumberOfReservation()));
        assertEquals(newReservation.getArrivalDate(), newArrivalDate);
        assertEquals(StatusCodeReservation.CODE_STATUS_PENDING_RESERVATION, newReservation.getStatusReservation());
    }

    @Test
    public void Should_ThrowNotFoundReservation_When_GivenNumberOfReservationNotExist() {
        String numberOfReservation = "123zxc";
        try {
            reservationService.cancelReservation(numberOfReservation);
        } catch (BusinessException e) {
            assertEquals("The reservation wasn't found: "+numberOfReservation, e.getMessage());
            return;
        }
        fail("Didn't throw the exception expected.");
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
            return;
        }
        fail("Didn't throw the exception expected.");
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

    @Test
    public void Should_ChangeStatusReservation_GivenAStatusReservation() {
        Reservation reservation = UtilTest.getReservationWithNumberOfReservation();
        reservationService.changeReservationsStatus(reservation, StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION);
        assertFalse(StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION.equals(reservation.getStatusReservation()));
    }

    @Test
    public void Should_DoNothing_GivenAReservationWithoutId() {
        Reservation reservation = UtilTest.getEmptyReservation();
        reservationService.changeReservationsStatus(reservation, StatusCodeReservation.CODE_STATUS_CANCEL_RESERVATION);
        verify(reservationRepository,never()).save(any());
    }
}
