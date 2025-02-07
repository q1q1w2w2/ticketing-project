package com.ticketing.project.service;

import com.ticketing.project.dto.concert.ConcertResponseDto;
import com.ticketing.project.dto.reservation.ReservationResponseDto;
import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.User;
import com.ticketing.project.execption.concert.ConcertNotFoundException;
import com.ticketing.project.execption.concert.InvalidConcertStatusException;
import com.ticketing.project.execption.reservation.SingleTicketPerUserException;
import com.ticketing.project.repository.ConcertRepository;
import com.ticketing.project.repository.ReservationRepository;
import com.ticketing.project.util.enums.QueueStatus;
import com.ticketing.project.util.websocket.QueueWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.ticketing.project.util.enums.ConcertStatus.RESERVATION_START;
import static com.ticketing.project.util.enums.QueueStatus.*;
import static com.ticketing.project.util.enums.TicketStatus.AVAILABLE;
import static java.util.concurrent.TimeUnit.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;
    private final QueueWebSocketHandler webSocketHandler;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    private static final String QUEUE_KEY = "reservationQueue:";
    private static final String QUEUE_ACTIVE_KEY = "activeQueue";
    private static final int MAX_USERS = 1000;
    private final ConcertRepository concertRepository;

    public boolean joinQueue(User user, Long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(ConcertNotFoundException::new);

        if (concert.getStatus() != RESERVATION_START) {
            throw new InvalidConcertStatusException("예매 가능한 상태가 아닙니다.");
        }

        if (reservationRepository.findByUserAndConcertAndStatus(user, concert, AVAILABLE).isPresent()) {
            throw new SingleTicketPerUserException();
        }

        String userKey = String.valueOf(user.getId());
        String queueKey = QUEUE_KEY + concertId;

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        if (operations.rank(queueKey, userKey) != null) {
            return true;
        }

        operations.add(queueKey, userKey, System.currentTimeMillis());
        redisTemplate.opsForSet().add(QUEUE_ACTIVE_KEY, concertId.toString());

        Long rank = retryGetRank(queueKey, userKey);

        if (rank == null) {
            throw new RuntimeException("대기열 참가 실패");
        } else {
            QueueStatus status = rank > MAX_USERS ? WAITING : COMP;
            webSocketHandler.sendJoinMessage(userKey, rank, status);
        }

        return false;
    }

    @Scheduled(fixedRate = 2000)
    public void controlQueueStatus() {
        Set<String> activeQueue = redisTemplate.opsForSet().members(QUEUE_ACTIVE_KEY);
        if (activeQueue == null || activeQueue.isEmpty()) {
            return;
        }
        log.info("스케줄러 반복 중");

        for (String concertId : activeQueue) {
            processQueueStatus(Long.parseLong(concertId));
        }
    }

    private void processQueueStatus(Long concertId) {
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
        Set<String> users = operations.range(QUEUE_KEY + concertId, 0, -1);

        if (users == null || users.isEmpty()) {
            return;
        }
        log.info("processQueueStatus 메서드 실행 중 ");

        for (String userKey : users) {
            Long rank = operations.rank(QUEUE_KEY + concertId, userKey);
            if (rank == null) continue;

            if (rank <= MAX_USERS) {
                reservationService.ticketing(concertId);
                webSocketHandler.sendJoinMessage(userKey, rank, COMP);
            } else {
                webSocketHandler.sendJoinMessage(userKey, rank, WAITING);
            }
        }
    }

    public void leaveQueue(User user, Long concertId) {
        String queueKey = QUEUE_KEY + concertId;
        String userKey = String.valueOf(user.getId());
        redisTemplate.opsForZSet().remove(queueKey, userKey);

        Long remainingUsers = redisTemplate.opsForZSet().size(queueKey);
        if (remainingUsers == null || remainingUsers == 0) {
            redisTemplate.opsForSet().remove(QUEUE_ACTIVE_KEY, concertId.toString());
        }
    }

    private Long retryGetRank(String queueKey, String userKey) {
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
        Long rank = null;
        int retryCount = 3;

        for (int i = 0; i < retryCount; i++) {
            rank = operations.rank(queueKey, userKey);
            if (rank != null) {
                return rank;
            }
        }
        return null;
    }

}
