package com.upgrade.campsite.mycamp;

import com.upgrade.campsite.mycamp.repository.Reservation;
import com.upgrade.campsite.mycamp.repository.ReservationsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationsRepositoryTests {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Test
    public void testSave() {
        LocalDateTime now = LocalDateTime.now();
        reservationsRepository.save(Reservation.builder().beginDateReservation(now).version(UUID.randomUUID()).build());
    }

}
