package com.ticketing.project.service;

import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.*;
import com.ticketing.project.enums.TicketStatus;
import com.ticketing.project.execption.reservation.ReservationNotFoundException;
import com.ticketing.project.execption.user.InvalidOwnerException;
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
                .status(AVAILABLE.value)
                .build();
        reservationRepository.save(reservation);

        return new ReservationResponseDto(concert, ticket);
    }

    @Transactional
    public void cancelReservation(Long reservationId, User user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);

        if (!reservation.getUser().equals(user)) {
            throw new InvalidOwnerException();
        }
        if (reservation.getStatus() == CANCEL.value) {
            throw new ReservationNotFoundException();
        }
        reservation.cancel();

        Ticket ticket = reservation.getTicket();
        ticket.cancel();

        Concert concert = reservation.getConcert();
        concert.decreaseReservedAmount();
    }

}
