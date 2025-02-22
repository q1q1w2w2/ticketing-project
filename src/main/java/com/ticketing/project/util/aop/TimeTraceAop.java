package com.ticketing.project.util.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class TimeTraceAop {

    @Around("execution(* com.ticketing.project.controller..*(..))")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long totalTimeMillis = stopWatch.getTotalTimeMillis();
            log.info("{}.{}: {}ms", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), totalTimeMillis);
        }
    }
}
