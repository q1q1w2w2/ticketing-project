package com.ticketing.project.service.reservation;

import com.ticketing.project.dto.ticket.TicketResponseDto;
import com.ticketing.project.entity.Ticket;
import com.ticketing.project.execption.ticket.TicketNotFoundException;
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
        return ticketRepository.save(new Ticket(UUID.randomUUID().toString(), AVAILABLE));
    }

    public TicketResponseDto findByUuid(String uuid) {
        Ticket ticket = ticketRepository.findBySerialNumber(uuid)
                .orElseThrow(TicketNotFoundException::new);
        return new TicketResponseDto(ticket);
    }
}
