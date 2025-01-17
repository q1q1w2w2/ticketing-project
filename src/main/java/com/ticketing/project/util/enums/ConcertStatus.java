package com.ticketing.project.util.enums;

public enum ConcertStatus {
    SCHEDULED(0),
    RESERVATION_START(1),
    RESERVATION_CLOSED(2),
    CANCELLED(3);

    public int value;

    ConcertStatus(int value) {
        this.value = value;
    }
}
