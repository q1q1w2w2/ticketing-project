package com.ticketing.project.util.aop;

import com.ticketing.project.entity.User;
import com.ticketing.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionLoggingAspect {

    private final UserService userService;

    @Around("@annotation(logUserAction)")
    public Object logUserAction(ProceedingJoinPoint joinPoint, LogUserAction logUserAction) throws Throwable {
        User user = userService.getCurrentUser();
        String methodName = joinPoint.getSignature().toShortString();
        String actionDescription = logUserAction.value();

        log.info("[User Action] {} 사용자가 [{}] 요청을 실행 ({})", user.getEmail(), actionDescription, methodName);
        return joinPoint.proceed();
    }
}
