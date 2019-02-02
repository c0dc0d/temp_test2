package com.upgrade.campsite.mycamp.repository;

import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationPeriodAvailableRepository extends CrudRepository<ReservationPeriodAvailable, Long> {

    @Query(" select rpa from ReservationPeriodAvailable rpa where " +
            " rpa.finalDateAvailable >= :departureDate and rpa.firstDateAvailable <= :arrivalDate ")
    List<ReservationPeriodAvailable> findAvailablePeriod(@Param("arrivalDate") LocalDate arrivalDate,
                                                         @Param("departureDate") LocalDate departureDate);

}
