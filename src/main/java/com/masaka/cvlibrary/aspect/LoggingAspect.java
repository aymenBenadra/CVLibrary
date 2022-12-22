package com.masaka.cvlibrary.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.masaka.cvlibrary.web.controller.*.*(..)) || execution(* com.masaka.cvlibrary.service.*.*(..)) || execution(* com.masaka.cvlibrary.repository.*.*(..))")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Entering {}.{}, with args: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), joinPoint.getArgs());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("Exiting {}.{}, with result: {}, in {} ms", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result, end - start);
        return result;
    }
}
