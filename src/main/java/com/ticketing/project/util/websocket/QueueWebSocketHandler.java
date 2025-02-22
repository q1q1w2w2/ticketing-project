package com.ticketing.project.util.websocket;

import com.ticketing.project.util.enums.QueueStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendJoinMessage(String destination, Long rank, QueueStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.toString());
        response.put("message", "대기열: " + (rank + 1));
        response.put("rank", rank + 1);

        messagingTemplate.convertAndSend("/topic/queue/" + destination, response);
    }
}
