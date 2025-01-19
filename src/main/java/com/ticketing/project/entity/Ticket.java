package com.ticketing.project.entity;

import com.ticketing.project.util.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ticketing.project.util.enums.TicketStatus.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "ticket")
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TicketStatus status;

    @PrePersist
    private void setIssueAt() {
        this.issueAt = LocalDateTime.now().withNano(0);
    }

    public Ticket(String serialNumber, TicketStatus status) {
        this.serialNumber = serialNumber;
        this.status = status;
    }

    public void cancel() {
        this.status = CANCEL;
    }

    public void expired() {
        this.status = EXPIRED;
    }
}
