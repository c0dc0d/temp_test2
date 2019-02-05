package com.upgrade.campsite.mycamp.utils;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;

import java.time.LocalDate;

public class UtilTest {

    public static Reservation getEmptyReservation() {
        return Reservation.builder().build();
    }

    public static Reservation getReservationWithMoreThanThreeDays() {
        return Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 7, 3))
                .departureDate(LocalDate.of(2019, 7, 9)).build();
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
