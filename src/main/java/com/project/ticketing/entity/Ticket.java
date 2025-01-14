package com.project.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int totalAmount;

    private int reservedAmount = 0;

//    @Version
//    private Integer version;

    public Ticket(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void increaseReservedAmount() {
        if (reservedAmount >= totalAmount) {
            throw new IllegalArgumentException("Sold Out!");
        }
        reservedAmount++;
    }

}
