package com.ticketing.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationFailureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private Long concertId;
    private String reason;
    private LocalDateTime failedAt;

    @PrePersist
    public void prePersist() {
        this.failedAt = LocalDateTime.now();
    }

    @Builder
    public ReservationFailureLog(String email, Long concertId, String reason) {
        this.email = email;
        this.concertId = concertId;
        this.reason = reason;
    }
}
