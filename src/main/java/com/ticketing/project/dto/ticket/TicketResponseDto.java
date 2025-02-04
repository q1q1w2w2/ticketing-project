package com.ticketing.project.dto.ticket;

import com.ticketing.project.entity.Ticket;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponseDto {
    private Long ticketId;
    private String serialNumber;
    private LocalDateTime issueAt;

    public TicketResponseDto(Ticket ticket) {
        this.ticketId = ticket.getId();
        this.serialNumber = ticket.getSerialNumber();
        this.issueAt = ticket.getIssueAt();
    }
}
