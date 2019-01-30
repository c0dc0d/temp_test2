package com.upgrade.campsite.mycamp.repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reservations", schema = "mycamp")
public class Reservetion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date dateReservetion;
}
