package com.ticketing.project.dto.concert;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.util.enums.ConcertStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConcertResponseDto {

    private Long id;
    private String title;
    private String locationName;
    private LocalDateTime concertAt;
    private int seats;
    private int reservedAmount;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private LocalDateTime createdAt;
    private ConcertStatus status;

    public ConcertResponseDto(Concert concert) {
        this.id = concert.getId();
        this.title = concert.getTitle();
        this.locationName = concert.getLocation().getLocationName();
        this.concertAt = concert.getConcertAt();
        this.seats = concert.getTotalAmount();
        this.reservedAmount = concert.getReservedAmount();
        this.openAt = concert.getOpenAt();
        this.closeAt = concert.getCloseAt();
        this.createdAt = concert.getCreatedAt();
        this.status = concert.getStatus();
    }
}
