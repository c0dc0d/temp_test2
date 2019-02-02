package com.upgrade.campsite.mycamp.repository;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface ReservationsRepository extends CrudRepository<Reservation, Long> {

    @Query(" select 1 from Reservation rsv where " +
            " rsv.statusReservation = '"+ StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION +"' "+
            "and ((rsv.arrivalDate  = :arrivalDate or rsv.departureDate = :departureDate) " +
            "or ((:arrivalDate >= rsv.arrivalDate and :arrivalDate < rsv.departureDate ) or (:departureDate > rsv.arrivalDate and :departureDate <= rsv.departureDate)) " +
            "or (:arrivalDate < rsv.arrivalDate and :departureDate > rsv.departureDate))")
    Integer existsReservation(@Param("arrivalDate") Date arrivalDate, @Param("departureDate") Date departureDate);

    Reservation findByNumberOfReservation(String numberOfReservation);
}
