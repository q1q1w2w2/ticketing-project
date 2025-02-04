package com.ticketing.project.dto.reservation;

import com.ticketing.project.dto.ticket.TicketResponseDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Reservation;
import com.ticketing.project.entity.Ticket;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponseDto {

    private String title;
    private LocalDateTime concertAt;
    private String address;
    private TicketResponseDto ticket;

    public ReservationResponseDto(Concert concert, Ticket ticket) {
        this.title = concert.getTitle();
        this.concertAt = concert.getConcertAt();
        this.address = concert.getLocation().getAddress();
        this.ticket = new TicketResponseDto(ticket);
    }

    public ReservationResponseDto(Reservation reservation) {
        this.title = reservation.getConcert().getTitle();
        this.concertAt = reservation.getConcert().getConcertAt();
        this.address = reservation.getConcert().getLocation().getAddress();
        this.ticket = new TicketResponseDto(reservation.getTicket());
    }
}
