package com.project.ticketing.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
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
        this.createdAt = LocalDateTime.now();
    }
}
