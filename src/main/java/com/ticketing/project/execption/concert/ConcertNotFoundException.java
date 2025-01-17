package com.ticketing.project.execption.concert;

import com.ticketing.project.execption.messages.ErrorMessages;

public class ConcertNotFoundException extends RuntimeException {
    public ConcertNotFoundException() {
        super(ErrorMessages.CONCERT_NOT_FOUND);
    }
}
