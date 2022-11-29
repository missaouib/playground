package com.github.yingzhuo.playground.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimedServiceAdvice {

    @Around("@annotation(annotation)")
    public Object exec(ProceedingJoinPoint joinPoint, TimedService annotation) throws Throwable {
        final long start = System.currentTimeMillis();
        final Object ret = joinPoint.proceed();
        final long end = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.info("耗时:{}毫秒", end - start);
        }
        return ret;
    }

}
