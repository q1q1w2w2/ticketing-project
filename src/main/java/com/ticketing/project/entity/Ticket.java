package com.ticketing.project.entity;

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

    @Column(name = "status")
    private int status;

    public Ticket(String serialNumber, int status) {
        this.serialNumber = serialNumber;
        this.status = status;
    }

    @PrePersist
    private void setIssueAt() {
        this.issueAt = LocalDateTime.now().withNano(0);
    }
}
