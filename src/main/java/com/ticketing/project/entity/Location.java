package com.ticketing.project.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "location")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "address")
    private String address;

    @Column(name = "total_seat")
    private int totalSeat;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void createdAt() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Location(String locationName, String address, int totalSeat) {
        this.locationName = locationName;
        this.address = address;
        this.totalSeat = totalSeat;
    }
}
