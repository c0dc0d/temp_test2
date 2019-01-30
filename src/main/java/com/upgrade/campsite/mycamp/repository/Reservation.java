package com.upgrade.campsite.mycamp.repository;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    @Column(unique = true)
    private LocalDateTime beginDateReservation;
    @Column(unique = true)
    private LocalDateTime enDateReservation;

    private UUID version;
}
