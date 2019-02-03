package com.upgrade.campsite.mycamp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "reservation_period_available")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationPeriodAvailable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDate firstDateAvailable;

    @Column(nullable = false)
    private LocalDate finalDateAvailable;

    private String statusAvailable;

}
