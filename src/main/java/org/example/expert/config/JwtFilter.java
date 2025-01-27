package org.example.expert.config;

import java.io.IOException;
import java.util.Collections;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String authorizationHeader = httpRequest.getHeader("Authorization");

        // 로그인 요청은 필터링 X
        if (requestURI.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("JWT 토큰이 필요합니다.");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            log.info("유효하지 않은 JWT 토큰입니다.");
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 JWT 토큰입니다.");
            return;
        }

        // 토큰에서 사용자 정보와 권한 추출
        Long userId = jwtUtil.extractUserId(jwt);
        String email = jwtUtil.extractEmail(jwt);
        String nickName = jwtUtil.extractNickName(jwt);
        String role = jwtUtil.extractRoles(jwt);

        if (nickName == null || role == null) {
            log.info("JWT 토큰에서 사용자 정보를 추출할 수 없습니다.");
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
            return;
        }

        UserRole userRole = UserRole.valueOf(role);

        // AuthUser 생성
        AuthUser authUser = new AuthUser(userId, email, nickName, userRole);

        User user = new User(nickName, "", Collections.singletonList(userRole::getRole));

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                authUser,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(userRole.getRole())))
        );

        chain.doFilter(request, response);
    }
}
