package com.ticketing.project.dto.reservation;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Ticket;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {

    private String title;
    private LocalDateTime concertAt;
    private String address;
    private Ticket ticket;

    public ReservationResponseDto(Concert concert, Ticket ticket) {
        this.title = concert.getTitle();
        this.concertAt = concert.getConcertAt();
        this.address = concert.getLocation().getAddress();
        this.ticket = ticket;
    }
}
