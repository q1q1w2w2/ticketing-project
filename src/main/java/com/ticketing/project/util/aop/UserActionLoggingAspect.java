package com.ticketing.project.util.aop;

import com.ticketing.project.entity.User;
import com.ticketing.project.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionLoggingAspect {

    private final UserService userService;

    @Around("@annotation(logUserAction)")
    public Object logUserAction(ProceedingJoinPoint joinPoint, LogUserAction logUserAction) throws Throwable {
        Logger logger = LoggerFactory.getLogger("user-action");

        User user = userService.getCurrentUser();
        String methodName = joinPoint.getSignature().toShortString();
        String actionDescription = logUserAction.value();

        String message = String.format("[UserAction] [%s]사용자가 [%s]요청을 실행 (%s)", user.getEmail(), actionDescription, methodName);
        logger.info(message);

        return joinPoint.proceed();
    }
}
