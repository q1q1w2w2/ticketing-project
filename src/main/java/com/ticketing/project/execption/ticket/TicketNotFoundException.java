package com.ticketing.project.execption.ticket;

import com.ticketing.project.execption.messages.ErrorMessages;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException() {
        super(ErrorMessages.TICKET_NOT_FOUND);
    }
}
