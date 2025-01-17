package com.ticketing.project.execption.user;

import com.ticketing.project.execption.messages.ErrorMessages;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ErrorMessages.USER_NOT_FOUND);
    }
}
