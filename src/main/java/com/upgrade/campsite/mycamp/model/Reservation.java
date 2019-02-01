package com.upgrade.campsite.mycamp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservations", schema = "mycamp")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date arrivalDate;

    @Column(nullable = false)
    private Date departureDate;

    private String statusReservation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private UUID version;
}
