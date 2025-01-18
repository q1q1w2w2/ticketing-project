package com.ticketing.project.execption.concert;

public class InvalidConcertStatusException extends RuntimeException {
    public InvalidConcertStatusException(String message) {
        super(message);
    }
}
