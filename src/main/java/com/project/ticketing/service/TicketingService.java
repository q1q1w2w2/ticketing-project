package com.project.ticketing.service;

import com.project.ticketing.entity.Reservation;
import com.project.ticketing.entity.Ticket;
import com.project.ticketing.repository.ReservationRepository;
import com.project.ticketing.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketingService {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void ticketing(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int ticketNumber = ticket.getReservedAmount();
        reservationRepository.save(new Reservation(ticket, ticketNumber));
    }
}
