package com.ticketing.project.util.rabbitmq;

import com.ticketing.project.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    public void sendReservation(Long concertId, User user) {
        log.info("Producer 호출!");
        ReservationRequest request = new ReservationRequest(concertId, user);
        rabbitTemplate.convertAndSend("reservationQueue", request);
    }
}
