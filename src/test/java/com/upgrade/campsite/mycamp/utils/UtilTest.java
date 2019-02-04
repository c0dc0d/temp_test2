package com.upgrade.campsite.mycamp.utils;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import java.time.LocalDate;

public class UtilTest {

    public static Reservation getReservationWithMoreThanThreeDays() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 7, 3))
                .departureDate(LocalDate.of(2019, 7, 9)).build();
    }

    public static Reservation getReservationWithJulyThreeDays() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 7, 3))
                .departureDate(LocalDate.of(2019, 7, 6)).build();
    }

    public static Reservation getReservationArrivalDateSameFirstDatePeriodAvailable() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 7, 1))
                .departureDate(LocalDate.of(2019, 7, 3)).build();
    }

    public static ReservationPeriodAvailable getPeriodAvailable() {
        return ReservationPeriodAvailable.builder()
                .firstDateAvailable(LocalDate.of(2019, 7, 1))
                .finalDateAvailable(LocalDate.of(2019, 7, 30)).build();
    }

    public static ReservationPeriodAvailable getPeriodJulyAvailable() {
        return ReservationPeriodAvailable.builder()
                .firstDateAvailable(LocalDate.of(2019, 7, 1))
                .finalDateAvailable(LocalDate.of(2019, 7, 30)).build();
    }

    public static Reservation getReservationThreeDaysInMay() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 5, 1))
                .departureDate(LocalDate.of(2019, 5, 3)).build();
    }

    public static Reservation getReservationWithNumberOfReservation() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 7, 3))
                .departureDate(LocalDate.of(2019, 7, 6))
                .numberOfReservation("123zxc")
                .statusReservation(StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION)
                .build();
    }
}