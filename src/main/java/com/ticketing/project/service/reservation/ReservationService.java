package com.ticketing.project.service.reservation;

import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.*;
import com.ticketing.project.execption.concert.ConcertNotFoundException;
import com.ticketing.project.execption.concert.InvalidConcertStatusException;
import com.ticketing.project.execption.reservation.NoAvailableSeatException;
import com.ticketing.project.execption.reservation.ReservationNotFoundException;
import com.ticketing.project.execption.reservation.SingleTicketPerUserException;
import com.ticketing.project.execption.user.InvalidOwnerException;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.ReservationRepository;
import com.ticketing.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final UserService userService;
    private final ReservationFailureLogService failureLogService;

    @Transactional
    public ReservationResponseDto ticketing(Long concertId) {
        User user = userService.getCurrentUser();
        try {
            Concert lockedConcert = concertRepository.findByIdForUpdate(concertId)
                    .orElseThrow(ConcertNotFoundException::new);

            if (lockedConcert.getStatus() != RESERVATION_START) {
                throw new InvalidConcertStatusException("예매 가능한 상태가 아닙니다.");
            }
            if (reservationRepository.findByUserAndConcertAndStatus(user, lockedConcert, AVAILABLE).isPresent()) {
                throw new SingleTicketPerUserException();
            }
            if (!lockedConcert.hasAvailableSeats()) {
                throw new NoAvailableSeatException();
            }

            lockedConcert.increasedReservedAmount();
            Ticket ticket = ticketService.generateTicket();
            Reservation reservation = Reservation.builder()
                    .user(user)
                    .concert(lockedConcert)
                    .ticket(ticket)
                    .status(AVAILABLE)
                    .build();
            return new ReservationResponseDto(reservationRepository.save(reservation));
        } catch (Exception e) {
            failureLogService.logFailedReservation(user.getEmail(), concertId, e.getMessage());
            throw e;
        }
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

    public List<ReservationResponseDto> getReservations(User user) {
        List<ReservationResponseDto> reservationsDto = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAllByUser(user);
        for (Reservation reservation : reservations) {
            reservationsDto.add(new ReservationResponseDto(reservation));
        }
        return reservationsDto;
    }
}
