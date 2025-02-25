package com.ticketing.project.interceptor;

import com.ticketing.project.execption.auth.TooManyRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
public class RequestLimitingInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_REQUEST = 10;
    private static final long LIMIT_TIME = 60;
    private static final String KEY_PREFIX = "request_limit";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        String ip = request.getRemoteAddr();
        String key = KEY_PREFIX + ip;

        Long requestCount = operations.increment(key);

        if (requestCount == null) {
            log.error("Redis INCR operation 실패");
            response.setStatus(INTERNAL_SERVER_ERROR.value());
            return false;
        }

        if (requestCount == 1) {
            redisTemplate.expire(key, LIMIT_TIME, TimeUnit.SECONDS);
        }

        if (requestCount > MAX_REQUEST) {
            throw new TooManyRequestException();
        }

        return true;
    }
}
