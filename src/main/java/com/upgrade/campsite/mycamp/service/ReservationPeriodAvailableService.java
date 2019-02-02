package com.upgrade.campsite.mycamp.service;

import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.repository.ReservationPeriodAvailableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationPeriodAvailableService {

    @Autowired
    private ReservationPeriodAvailableRepository reservationPeriodAvailableRepository;

    @Transactional
    public ReservationPeriodAvailable save(ReservationPeriodAvailable reservationPeriodAvailable){
        return reservationPeriodAvailableRepository.save(reservationPeriodAvailable);
    }

    public List<ReservationPeriodAvailable> findAvailablePeriod(LocalDate arrivalDate, LocalDate departureDate) {
        return reservationPeriodAvailableRepository.findAvailablePeriod(arrivalDate, departureDate);
    }

    public List<ReservationPeriodAvailable> findAll() {
        List<ReservationPeriodAvailable> listPeriod = new ArrayList<>();
        reservationPeriodAvailableRepository.findAll().forEach(rpa -> listPeriod.add(rpa));
        return listPeriod;
    }

}
