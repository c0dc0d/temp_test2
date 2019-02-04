package com.upgrade.campsite.mycamp.repository;

import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservationPeriodAvailableRepository extends CrudRepository<ReservationPeriodAvailable, Long> {

    @Query(" select rpa from ReservationPeriodAvailable rpa where " +
            " rpa.statusAvailable = 'Y' " +
            " and rpa.finalDateAvailable >= :departureDate and rpa.firstDateAvailable <= :arrivalDate ")
    ReservationPeriodAvailable findAvailablePeriod(@Param("arrivalDate") LocalDate arrivalDate,
                                                         @Param("departureDate") LocalDate departureDate);


    ReservationPeriodAvailable findByStatusAvailable(String statusAvailable);
}
