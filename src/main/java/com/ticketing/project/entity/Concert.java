package com.ticketing.project.entity;

import com.ticketing.project.execption.reservation.NoAvailableSeatException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Concert {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "title")
    private String title;

    @Column(name = "concert_at")
    private LocalDateTime concertAt;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private int totalAmount;

    private int reservedAmount = 0;

    @PrePersist
    private void createdAt() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Concert(Location location, String title, LocalDateTime concertAt, LocalDateTime openAt, LocalDateTime closeAt, int totalAmount) {
        this.location = location;
        this.title = title;
        this.concertAt = concertAt;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.totalAmount = totalAmount;
    }

    public void increasedReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new NoAvailableSeatException();
        }
        reservedAmount++;
    }
}
