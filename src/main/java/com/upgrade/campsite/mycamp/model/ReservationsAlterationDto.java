package com.upgrade.campsite.mycamp.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationsAlterationDto {

    private String numberOfReservation;
    private LocalDate newArrivalDate;
    private LocalDate newDepartureDate;

}
