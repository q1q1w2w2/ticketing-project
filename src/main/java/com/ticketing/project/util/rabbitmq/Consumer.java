package com.ticketing.project.util.rabbitmq;

import com.ticketing.project.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Consumer {

    private final ReservationService reservationService;

    @RabbitListener(queues = "reservationQueue")
    public void receiveReservation(ReservationRequest request) {
        log.info("Consumer 호출!");
        try {
            reservationService.ticketing(request.getConcertId(), request.getUser());
            log.info("티켓팅 성공!");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
