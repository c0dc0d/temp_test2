package com.upgrade.campsite.mycamp.model.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationsStatusDto implements Serializable {

    public String numberOfReservation;
    public Boolean reservationAcceptance;
}
