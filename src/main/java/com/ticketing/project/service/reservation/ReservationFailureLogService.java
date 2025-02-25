package com.ticketing.project.service.reservation;

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
    public void logFailedReservation(String email, Long concertId, String failureReason) {
        ReservationFailureLog log = ReservationFailureLog.builder()
                .email(email)
                .concertId(concertId)
                .reason(failureReason)
                .build();
        failureLogRepository.save(log);
    }
}
