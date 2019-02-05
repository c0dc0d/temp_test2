package com.upgrade.campsite.mycamp.repository;

import com.upgrade.campsite.mycamp.constants.StatusCodeReservation;
import com.upgrade.campsite.mycamp.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query(" select 1 from Reservation rsv where " +
            " rsv.statusReservation = '"+ StatusCodeReservation.CODE_STATUS_ACCEPT_RESERVATION +"' "+
            "and ((rsv.arrivalDate  = :arrivalDate or rsv.departureDate = :departureDate) " +
            "or ((:arrivalDate >= rsv.arrivalDate and :arrivalDate < rsv.departureDate ) or (:departureDate > rsv.arrivalDate and :departureDate <= rsv.departureDate)) " +
            "or (:arrivalDate < rsv.arrivalDate and :departureDate > rsv.departureDate))")
    Integer existsReservation(@Param("arrivalDate") LocalDate arrivalDate, @Param("departureDate") LocalDate departureDate);

    Reservation findByNumberOfReservation(String numberOfReservation);

    @Query(" select rsv from Reservation rsv where rsv.statusReservation = :statusReservation " +
            "and (rsv.arrivalDate >= :startDate or rsv.departureDate >= :startDate) " +
            "and (rsv.departureDate <= :endDate or rsv.arrivalDate <= :endDate) " +
            "order by rsv.arrivalDate asc")
    List<Reservation> findByDateRangeAndStatusReservation(
            @Param("statusReservation") String statusReservation,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
