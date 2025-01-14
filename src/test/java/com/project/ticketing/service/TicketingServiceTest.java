package com.project.ticketing.service;

import com.project.ticketing.entity.Ticket;
import com.project.ticketing.repository.ReservationRepository;
import com.project.ticketing.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TicketingServiceTest {

    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("티켓 동시 요청 성공")
    void ticketingTest() throws InterruptedException {
        // given
        int memberCount = 30;
        int ticketAmount = 10;
        Ticket ticket = ticketRepository.save(new Ticket(ticketAmount));

        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
                try {
                    ticketingService.ticketing(ticket.getId());
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

        // then
//        assertThat(reservationRepository.count()).isEqualTo(Math.min(memberCount, ticketAmount));
        assertThat(reservationRepository.count()).isEqualTo(ticketAmount);
    }
}