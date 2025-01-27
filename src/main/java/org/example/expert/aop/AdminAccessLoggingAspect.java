package org.example.expert.aop;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

    private final HttpServletRequest request;

    // Level 3) 코드 개선 퀴즈 - AOP의 이해
    @Before("execution(* org.example.expert.domain.user.controller.UserController.getUser(..))")
    public void logAfterChangeUserRole(JoinPoint joinPoint) {
        String userId = String.valueOf(request.getAttribute("userId"));
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                userId,requestTime, requestUrl, joinPoint.getSignature().getName());
    }
}
