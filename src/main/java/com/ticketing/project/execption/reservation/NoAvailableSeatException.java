package com.ticketing.project.execption.reservation;

import com.ticketing.project.execption.messages.ErrorMessages;

public class NoAvailableSeatException extends RuntimeException {
    public NoAvailableSeatException() {
        super(ErrorMessages.NO_AVAILABLE_SEAT);
    }

    public NoAvailableSeatException(String message) {
        super(message);
    }
}
