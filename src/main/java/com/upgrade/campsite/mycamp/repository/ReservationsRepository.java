package com.upgrade.campsite.mycamp.repository;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservation, Long> {

    @Query(" select 1 from Reservation rsv where " +
            " rsv.statusReservation = '"+ StatusCodeReservation.CODE_STATUS_CONFIRMED_RESERVATION +"' "+
            "and ((rsv.arrivalDate  = :arrivalDate or rsv.departureDate = :departureDate) " +
            "or (:arrivalDate between rsv.arrivalDate and rsv.departureDate or :departureDate between rsv.arrivalDate and rsv.departureDate) " +
            "or (:arrivalDate < rsv.arrivalDate and :departureDate > rsv.departureDate))")
    Integer existsReservation(@Param("arrivalDate") Date arrivalDate, @Param("departureDate") Date departureDate);

}
