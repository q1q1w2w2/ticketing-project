package com.ticketing.project.entity;

import com.ticketing.project.util.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ticketing.project.util.enums.TicketStatus.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

//    @Column(name = "user_id")
//    private Long userId;
//
//    @Column(name = "concert_id")
//    private Long concertId;
//
//    @Column(name = "ticket_id")
//    private Long ticketId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TicketStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    private void createdAt() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Reservation(User user, Concert concert, Ticket ticket, TicketStatus status) {
        this.user = user;
        this.concert = concert;
        this.ticket = ticket;
        this.status = status;
    }

    public void cancel() {
        this.status = CANCEL;
    }

    public void expired() {
        this.status = EXPIRED;
    }
}
