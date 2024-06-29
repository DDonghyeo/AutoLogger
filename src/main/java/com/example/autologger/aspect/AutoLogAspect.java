package com.example.autologger.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class AutoLogAspect {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> DEPTH = new ThreadLocal<>();

    @Around("@annotation(com.example.autologger.annotaion.AutoLog) || " +
            "within(@org.springframework.stereotype.Service *) || " +
            "within(@org.springframework.stereotype.Repository *) || " +
            "within(@org.springframework.stereotype.Component *)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName(); //메서드 이름
        String className = joinPoint.getTarget().getClass().getSimpleName(); //클래스 이름

        String requestId = REQUEST_ID.get();
        if (requestId == null) {
            //랜덤 ID 생성
            requestId = UUID.randomUUID().toString().substring(0, 8);
            REQUEST_ID.set(requestId);
        }

        //깊이
        int depth = DEPTH.get() == null ? 1 : DEPTH.get() + 1;
        DEPTH.set(depth);

        //시작 시간 기록
        long startTime = System.currentTimeMillis();

        //시작할 때 로그 출력
        log.info("[{}] Depth: {} - {}.{}()", requestId, depth, className, methodName);

        Object result = null;
        try {
            //프로세스 실행
            result = joinPoint.proceed();
            //실행 후 결과 출력
            log.info("[{}] Depth: {} - {}.{}() Status: Success, Duration: {} ms",
                    requestId, depth, className, methodName, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            //예외 출력
            log.error("[{}] Depth: {} - {}.{}() failed. Exception: {}:{}",
                    requestId, depth, className, methodName, e.getClass(), e.getMessage());
            throw e;

        } finally {
            DEPTH.set(depth - 1);
            if (depth == 1) {
                REQUEST_ID.remove();
                DEPTH.remove();
            }
        }
        return result;
    }
}
