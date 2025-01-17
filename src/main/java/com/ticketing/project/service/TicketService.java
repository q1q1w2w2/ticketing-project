package com.ticketing.project.service;

import com.ticketing.project.entity.Ticket;
import com.ticketing.project.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ticketing.project.util.enums.TicketStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional
    public Ticket generateTicket() {
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), AVAILABLE.value);
        return ticketRepository.save(ticket);
    }
}
