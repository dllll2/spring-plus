package org.example.expert.domain.user.enums;

import java.util.Arrays;

import org.example.expert.domain.common.exception.InvalidRequestException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    USER("ROLE_USER", "사용자 권한")
    ;

    private final String role;
    private final String description;

    public static UserRole of(String role) {
        log.info("전달된 UserRole 값: {}", role);
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
    }
}
