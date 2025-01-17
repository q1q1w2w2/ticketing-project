package com.ticketing.project.execption.reservation;

import com.ticketing.project.execption.messages.ErrorMessages;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException() {
        super(ErrorMessages.RESERVATION_NOT_FOUND);
    }
}
