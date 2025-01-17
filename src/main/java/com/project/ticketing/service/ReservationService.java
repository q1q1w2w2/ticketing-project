package com.project.ticketing.service;

import com.project.ticketing.entity.*;
import com.project.ticketing.repository.ConcertRepository;
import com.project.ticketing.repository.ReservationRepository;
import com.project.ticketing.repository.TicketRepository;
import com.project.ticketing.util.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.project.ticketing.util.enums.ReservationStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public Ticket ticketing(Long concertId, User user) {
        Concert concert = concertRepository.findByIdForUpdate(concertId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연을 찾을 수 없습니다."));

        concert.increasedReservedAmount();

        Ticket ticket = new Ticket(UUID.randomUUID().toString());
        ticketRepository.save(ticket);

        Reservation reservation = Reservation.builder()
                .user(user)
                .concert(concert)
                .ticket(ticket)
                .status(AVAILABLE)
                .build();
        reservationRepository.save(reservation);

        return ticket;
    }


}
