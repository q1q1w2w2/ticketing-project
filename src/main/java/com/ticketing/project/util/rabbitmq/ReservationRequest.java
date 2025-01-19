package com.ticketing.project.util.rabbitmq;

import com.ticketing.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ReservationRequest implements Serializable {

    private Long concertId;
    private User user;
}
