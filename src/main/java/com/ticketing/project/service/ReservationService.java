package com.ticketing.project.service;

import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.*;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ticketing.project.enums.TicketStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final TicketService ticketService;

    @Transactional
    public ReservationResponseDto ticketing(Long concertId, User user) {
        Concert concert = concertRepository.findByIdForUpdate(concertId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연을 찾을 수 없습니다."));

        concert.increasedReservedAmount();

        Ticket ticket = ticketService.generateTicket();

        Reservation reservation = Reservation.builder()
                .user(user)
                .concert(concert)
                .ticket(ticket)
                .build();
        reservationRepository.save(reservation);

        return new ReservationResponseDto(concert, ticket);
    }


}
