package com.ticketing.project.service;

import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.*;
import com.ticketing.project.execption.concert.ConcertNotFoundException;
import com.ticketing.project.execption.concert.InvalidConcertStatusException;
import com.ticketing.project.execption.reservation.ReservationNotFoundException;
import com.ticketing.project.execption.reservation.SingleTicketPerUserException;
import com.ticketing.project.execption.user.InvalidOwnerException;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.ReservationRepository;
import com.ticketing.project.util.enums.ConcertStatus;
import com.ticketing.project.util.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ticketing.project.util.enums.ConcertStatus.*;
import static com.ticketing.project.util.enums.TicketStatus.*;

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
                .orElseThrow(ConcertNotFoundException::new);

        if (!concert.getStatus().equals(RESERVATION_START)) {
            throw new InvalidConcertStatusException("예매 가능한 상태가 아닙니다.");
        }

        if (reservationRepository.findByUserAndConcertAndStatus(user, concert, AVAILABLE).isPresent()) {
            throw new SingleTicketPerUserException();
        }

        concert.increasedReservedAmount();

        Ticket ticket = ticketService.generateTicket();

        Reservation reservation = Reservation.builder()
                .user(user)
                .concert(concert)
                .ticket(ticket)
                .status(AVAILABLE)
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
        if (reservation.getStatus() == CANCEL || reservation.getStatus() == EXPIRED) {
            throw new ReservationNotFoundException();
        }

        Ticket ticket = reservation.getTicket();
        ticket.cancel();
        reservation.cancel();

        Concert concert = concertRepository.findByIdForUpdate(reservation.getConcert().getId())
                .orElseThrow(ConcertNotFoundException::new);
        concert.decreaseReservedAmount();
    }

    @Transactional
    public void cancelReservations(Concert concert) {
        List<Reservation> reservations = reservationRepository.findAllByConcert(concert);

        for (Reservation reservation : reservations) {
            reservation.cancel();
            reservation.getTicket().cancel();
        }
    }

}
