package com.ticketing.project.entity;

import com.ticketing.project.execption.reservation.NoAvailableSeatException;
import com.ticketing.project.util.enums.ConcertStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "concert")
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ConcertStatus status;

    @Column(name = "totalAmount")
    private int totalAmount;

    @Column(name = "reservedAmount")
    private int reservedAmount = 0;

    @PrePersist
    private void createdAt() {
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Builder
    public Concert(Location location, String title, LocalDateTime concertAt, LocalDateTime openAt, LocalDateTime closeAt, int totalAmount, ConcertStatus status) {
        this.location = location;
        this.title = title;
        this.concertAt = concertAt;
        setOpenAt(openAt);
        setCloseAt(closeAt);
        this.totalAmount = totalAmount;
        this.status = status;
    }

    private void setOpenAt(LocalDateTime openAt) {
        if (openAt.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("예매 시작 시간은 30분 단위로 설정해야 합니다.");
        }
        this.openAt = openAt;
    }

    private void setCloseAt(LocalDateTime closeAt) {
        if (closeAt.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("예매 종료 시간은 30분 단위로 설정해야 합니다.");
        }
        this.closeAt = closeAt;
    }

    public void increasedReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new NoAvailableSeatException();
        }
        reservedAmount++;
    }

    public void decreaseReservedAmount() {
        if (reservedAmount <= 0) {
            throw new NoAvailableSeatException("예약된 좌석이 없습니다.");
        }
        reservedAmount--;
    }

    public void changeStatus(ConcertStatus status) {
        this.status = status;
    }
}
