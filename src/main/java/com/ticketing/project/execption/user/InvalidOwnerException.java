package com.ticketing.project.execption.user;

import com.ticketing.project.execption.messages.ErrorMessages;

public class InvalidOwnerException extends RuntimeException {
    public InvalidOwnerException() {
        super(ErrorMessages.INVALID_OWNER);
    }
}
