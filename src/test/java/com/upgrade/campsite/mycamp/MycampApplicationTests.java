package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.exceptions.BusinessException;
import com.upgrade.campsite.mycamp.model.Reservation;
import com.upgrade.campsite.mycamp.model.ReservationPeriodAvailable;
import com.upgrade.campsite.mycamp.service.ReservationPeriodAvailableService;
import com.upgrade.campsite.mycamp.service.ReservationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MycampApplicationTests {

    @Autowired
	private ReservationService reservationService;

    @Autowired
    private ReservationPeriodAvailableService reservationPeriodAvailableService;

    @Before
    public void setUp() {
        reservationPeriodAvailableService.save(
                ReservationPeriodAvailable.builder()
                    .firstDateAvailable(LocalDate.of(2019,3,1))
                    .finalDateAvailable(LocalDate.of(2019,3,30))
                    .build()
        );
    }

	@Test
	public void shouldSendMessageThenProcessingQueue() throws BusinessException {
        Reservation reservaiton = Reservation.builder()
                .arrivalDate(LocalDate.of(2019, 3, 1))
                .departureDate(LocalDate.of(2019, 3, 3)).build();
        Reservation reservationSaved = reservationService.sendProcessing(reservaiton);
        assertNotNull(reservationSaved);
    }

}

