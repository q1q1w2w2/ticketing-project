package com.project.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "issue_at")
    private LocalDateTime issueAt;

    public Ticket(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @PrePersist
    private void setIssueAt() {
        this.issueAt = LocalDateTime.now().withNano(0);
    }
}
