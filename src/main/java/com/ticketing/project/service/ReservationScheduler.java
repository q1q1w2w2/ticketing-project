package com.ticketing.project.service;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Reservation;
import com.ticketing.project.entity.Ticket;
import com.ticketing.project.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ticketing.project.util.enums.TicketStatus.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void expiredTicketAndReservation() {
        LocalDateTime now = LocalDateTime.now();
        log.info("** 티켓 만료 로직 실행됨[{}] **", now);

        List<Reservation> reservations = reservationRepository.findAllByStatus(AVAILABLE);
        for (Reservation reservation : reservations) {
            Concert concert = reservation.getConcert();
            LocalDateTime concertAt = concert.getConcertAt();
            if (concertAt.isBefore(now)) {
                Ticket ticket = reservation.getTicket();
                ticket.expired();
                reservation.expired();
            }
        }
    }
}
