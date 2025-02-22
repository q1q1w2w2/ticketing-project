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
    @Column(name = "failure_log_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "fail_reason")
    private String reason;

    @Column(name = "failed_at")
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
