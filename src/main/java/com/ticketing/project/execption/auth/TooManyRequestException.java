package com.ticketing.project.execption.auth;

import com.ticketing.project.execption.messages.ErrorMessages;

public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException() {
        super(ErrorMessages.TOO_MANY_REQUEST);
    }
}
