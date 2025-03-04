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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ticketing.project.util.enums.ConcertStatus.*;
import static com.ticketing.project.util.enums.TicketStatus.*;

@Slf4j
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
            validateReservationConditions(lockedConcert, user);

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
        validateCancelReservationConditions(user, reservation);

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

    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllByUser(userService.getCurrentUser());
        return reservations.stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    public List<ReservationResponseDto> getReservations(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        List<Reservation> reservations = reservationRepository.findAllByUser(userService.getCurrentUser(), pageRequest);
        return reservations.stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    private void validateReservationConditions(Concert lockedConcert, User user) {
        if (lockedConcert.getStatus() != RESERVATION_START) {
            throw new InvalidConcertStatusException("예매 가능한 상태가 아닙니다.");
        }
        if (reservationRepository.findByUserAndConcertAndStatus(user, lockedConcert, AVAILABLE).isPresent()) {
            throw new SingleTicketPerUserException();
        }
        if (!lockedConcert.hasAvailableSeats()) {
            throw new NoAvailableSeatException();
        }
    }

    private void validateCancelReservationConditions(User user, Reservation reservation) {
        if (!reservation.getUser().equals(user)) {
            throw new InvalidOwnerException();
        }
        if (reservation.getStatus() == CANCEL || reservation.getStatus() == EXPIRED) {
            throw new ReservationNotFoundException();
        }
    }
}
