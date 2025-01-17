package com.ticketing.project.execption.auth;

import static com.ticketing.project.execption.messages.ErrorMessages.INVALID_TOKEN;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException() {
        super(INVALID_TOKEN);
    }

    public TokenValidationException(String message) {
        super(message);
    }
}
