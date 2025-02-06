package com.ticketing.project.service;

import com.ticketing.project.entity.User;
import com.ticketing.project.util.enums.QueueStatus;
import com.ticketing.project.util.websocket.QueueWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.ticketing.project.util.enums.QueueStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, String> redisTemplate;
    private final QueueWebSocketHandler webSocketHandler;

    private static final String QUEUE_KEY = "reservationQueue";
    private static final String QUEUE_ACTIVE_KEY = "queueActive";
    private static final int MAX_USERS = 1000;

    public boolean joinQueue(User user) {
        String userKey = String.valueOf(user.getId());
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        if (operations.rank(QUEUE_KEY, userKey) != null) {
            return true;
        }

        operations.add(QUEUE_KEY, userKey, System.currentTimeMillis());
        redisTemplate.opsForValue().set(QUEUE_ACTIVE_KEY, "true");

        Long rank = retryGetRank(userKey);

        if (rank == null) {
            throw new RuntimeException("대기열 참가 실패");
        } else {
            QueueStatus status = rank > MAX_USERS ? WAITING : ALLOWED;
            webSocketHandler.sendJoinMessage(userKey, rank, status);
        }

        return false;
    }

    @Scheduled(fixedRate = 2000)
    public void controlQueueStatus() {
        log.info("size = {}", redisTemplate.opsForZSet().size(QUEUE_KEY));
        boolean isActive = Boolean.TRUE.equals(Boolean.valueOf(redisTemplate.opsForValue().get(QUEUE_ACTIVE_KEY)));
        if (!isActive) {
            return;
        }
        log.info("스케줄러 반복 중");

        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
        Set<String> users = operations.range(QUEUE_KEY, 0, -1);

        if (users == null || users.isEmpty()) {
            redisTemplate.opsForValue().set(QUEUE_ACTIVE_KEY, "false");
            return;
        }

        for (String userKey : users) {
            Long rank = operations.rank(QUEUE_KEY, userKey);
            if (rank == null) continue;

            QueueStatus status = rank < MAX_USERS ? ALLOWED : WAITING;
            webSocketHandler.sendJoinMessage(userKey, rank, status);
        }
    }

    public void leaveQueue(User user) {
        String userKey = String.valueOf(user.getId());
        redisTemplate.opsForZSet().remove(QUEUE_KEY, userKey);

        Long remainingUsers = redisTemplate.opsForZSet().size(QUEUE_KEY);
        if (remainingUsers == null || remainingUsers == 0) {
            redisTemplate.opsForValue().set(QUEUE_ACTIVE_KEY, "false");
        }
    }

    private Long retryGetRank(String userKey) {
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
        Long rank = null;
        int retryCount = 3;
        int delay = 100;

        for (int i = 0; i < retryCount; i++) {
            rank = operations.rank(QUEUE_KEY, userKey);
            if (rank != null) {
                return rank;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

}
