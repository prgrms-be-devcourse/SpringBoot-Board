package com.example.board.domain.auth.service;

import com.example.board.domain.auth.dto.TokenResponse;
import com.example.board.global.exception.CustomException;
import com.example.board.global.exception.ErrorCode;
import com.example.board.global.security.jwt.provider.JwtTokenProvider;
import com.example.board.global.util.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenResponse reissueToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String redisRefreshToken = redisService.getData(authentication.getName());

        if (redisRefreshToken != null && !redisRefreshToken.equals(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_EXIST);
        }
        String newAccessToken = jwtTokenProvider.createToken(authentication, "access");
        Authentication newAuthentication = jwtTokenProvider.getAuthentication(newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        return new TokenResponse(
            newAccessToken,
            jwtTokenProvider.createToken(authentication, "refresh")
        );
    }
}
