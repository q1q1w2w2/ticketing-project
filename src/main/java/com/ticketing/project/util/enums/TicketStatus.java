package com.ticketing.project.util.enums;

public enum TicketStatus {
    AVAILABLE(0),
    CANCEL(1),
    EXPIRED(2);

    public int value;

    TicketStatus(int value) {
        this.value = value;
    }
}
