package org.example.expert.config;

import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;

    // @Bean
    // public FilterRegistrationBean<JwtFilter> jwtFilter() {
    //     FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
    //     registrationBean.setFilter(new JwtFilter(jwtUtil));
    //     registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴을 지정합니다.
    //
    //     return registrationBean;
    // }
}
