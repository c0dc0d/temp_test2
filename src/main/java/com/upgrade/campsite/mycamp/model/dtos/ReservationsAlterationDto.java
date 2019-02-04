package com.upgrade.campsite.mycamp.model.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationsAlterationDto implements Serializable {

    private String numberOfReservation;
    private LocalDate newArrivalDate;
    private LocalDate newDepartureDate;

}
