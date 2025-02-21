package com.ticketing.project.service;

import com.ticketing.project.entity.ReservationFailureLog;
import com.ticketing.project.repository.ReservationFailureLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationFailureLogService {

    private final ReservationFailureLogRepository failureLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedReservation(String email, Long concertId, String reason) {
        ReservationFailureLog log = ReservationFailureLog.builder()
                .email(email)
                .concertId(concertId)
                .reason(reason)
                .build();
        failureLogRepository.save(log);
    }
}
