package com.ticketing.project.execption.reservation;

import com.ticketing.project.execption.messages.ErrorMessages;

public class SingleTicketPerUserException extends RuntimeException {
    public SingleTicketPerUserException() {
        super(ErrorMessages.SINGLE_TICKET_PER_USER);
    }
}
