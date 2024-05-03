package com.programmers.board.config;

import com.programmers.board.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Objects;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditConfig {
    @Bean
    public AuditorAware<Long> auditorProvider(HttpServletRequest httpServletRequest) {
        return () -> {
            HttpSession session = httpServletRequest.getSession(false);
            if (Objects.isNull(session)) {
                return Optional.empty();
            }
            Long loginUserId = (Long) session.getAttribute(SessionConst.LOGIN_USER_ID);
            if (Objects.isNull(loginUserId)) {
                return Optional.empty();
            }
            return Optional.of(loginUserId);
        };
    }
}
