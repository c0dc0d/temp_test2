package com.upgrade.campsite.mycamp.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationsStatusDto {

    public String numberOfReservation;
    public Boolean reservationAcceptance;
}
