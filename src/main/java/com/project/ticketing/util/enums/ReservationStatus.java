package com.project.ticketing.util.enums;

public enum ReservationStatus {
    AVAILABLE(0),
    EXPIRED(1);

    public int value;

    ReservationStatus(int value) {
        this.value = value;
    }
}
