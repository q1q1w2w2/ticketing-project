package com.ticketing.project.service;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Location;
import com.ticketing.project.entity.User;
import com.ticketing.project.repository.*;
import com.ticketing.project.service.reservation.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ticketing.project.util.enums.ConcertStatus.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("티켓팅 동시 요청 성공")
    void ticketing() throws InterruptedException {
        // given
        int userCount = 30;
        int remainAmount = 10;

        Location location = Location.builder()
                .locationName("공연장")
                .address("주소")
                .totalSeat(remainAmount)
                .build();
        Location savedLocation = locationRepository.save(location);

        Concert concert = Concert.builder()
                .location(savedLocation)
                .title("공연1")
                .concertAt(LocalDateTime.of(2025, 1, 30, 21, 0))
                .openAt(LocalDateTime.of(2025, 1, 10, 10, 0))
                .closeAt(LocalDateTime.of(2025, 1, 11, 10, 0))
                .status(RESERVATION_START)
                .totalAmount(savedLocation.getTotalSeat())
                .build();
        Concert savedConcert = concertRepository.save(concert);

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(userCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < userCount; i++) {
            User user = User.builder()
                    .email("email+" + i + "@gmail.com")
                    .password("pwd")
                    .tel("010-1234-1234")
                    .build();
            User savedUser = userRepository.save(user);

            executorService.submit(() -> {
                try {
                    reservationService.ticketing(savedConcert.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        Concert foundConcert = concertRepository.findById(savedConcert.getId()).orElseThrow();

        // then
        assertThat(reservationRepository.count()).isEqualTo(remainAmount);
        assertThat(foundConcert.getReservedAmount()).isEqualTo(remainAmount);
    }
}