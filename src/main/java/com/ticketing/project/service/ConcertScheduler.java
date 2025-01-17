package com.ticketing.project.service;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.util.enums.ConcertStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.ticketing.project.util.enums.ConcertStatus.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConcertScheduler {

    private final ConcertRepository concertRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void updateConcertStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info("현재 시각: {}", now);

        List<Concert> concerts = concertRepository.findAllByStatusNotIn(
                Arrays.asList(CANCELLED, RESERVATION_CLOSED)
        );

        for (Concert concert : concerts) {
            if(now.isAfter(concert.getOpenAt()) && now.isBefore(concert.getCloseAt())) {
                concert.changeStatus(RESERVATION_START);
            } else if (now.isAfter(concert.getCloseAt())) {
                concert.changeStatus(RESERVATION_CLOSED);
            }
        }
    }
}
