package com.ticketing.project.service;

import com.ticketing.project.entity.User;
import com.ticketing.project.service.reservation.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class LoadTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    public void testLoad() throws InterruptedException {
        int userCount = 100000;
        ExecutorService es = Executors.newFixedThreadPool(100);

        for (int i = 0; i < userCount; i++) {
            User user = new User("name", "email", "password", "010", "USER");

//            es.submit(() -> {
//                Assertions.assertDoesNotThrow(() -> producer.sendReservation(1L, user));
//            } );
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);
    }

    @Test
    public void testLoad2() throws InterruptedException {
        int userCount = 100000;
        ExecutorService es = Executors.newFixedThreadPool(100);

        for (int i = 0; i < userCount; i++) {
            User user = new User("name", "email", "password", "010", "USER");

            es.submit(() -> {
                Assertions.assertDoesNotThrow(() -> reservationService.ticketing(1L));
            } );
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);
    }
}
