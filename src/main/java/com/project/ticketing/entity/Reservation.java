package com.project.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long ticketId;

    private int ticketNumber;

    public Reservation(Ticket ticket, int ticketNumber) {
        this.ticketId = ticket.getId();
        this.ticketNumber = ticketNumber;
    }
}
