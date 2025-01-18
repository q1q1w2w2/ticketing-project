package com.ticketing.project.execption.concert;

public class InvalidConcertTimeException extends RuntimeException {
    public InvalidConcertTimeException(String message) {
        super(message);
    }
}
