package com.example.board.global.security.jwt;

import com.example.board.global.exception.BusinessException;
import com.example.board.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Jwt jwt;  // jwt 디코딩에 이용

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getToken(request);
            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    log.debug("Jwt parse result : {}", claims);

                    String requestURI = request.getRequestURI();
                    if (claims.tokenType.equals(TokenType.REFRESH.name()) && !requestURI.equals("/api/v1/reissue")) {
                        throw new BusinessException(ErrorCode.NOT_EXPECTED_TOKEN_TYPE);
                    }

                    String username = claims.username;
                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    if (isNotEmpty(username) && authorities.size() > 0) {
                        JwtAuthenticationToken authentication
                                = new JwtAuthenticationToken(new JwtAuthentication(username, token), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // 필수는 아니라함
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                } catch (Exception e) {
                    log.warn("Jwt processing failed: {}", e.getMessage());
                }
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained : {}",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (isNotEmpty(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            log.debug("Jwt token detected : {}", token);
            try {
                return URLDecoder.decode(token, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0
                ? emptyList()
                : Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }
}
