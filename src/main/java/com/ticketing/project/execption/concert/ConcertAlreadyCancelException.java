package com.ticketing.project.execption.concert;

import com.ticketing.project.execption.messages.ErrorMessages;

public class ConcertAlreadyCancelException extends RuntimeException {
    public ConcertAlreadyCancelException() {
        super(ErrorMessages.ALREADY_CANCEL);
    }
}
