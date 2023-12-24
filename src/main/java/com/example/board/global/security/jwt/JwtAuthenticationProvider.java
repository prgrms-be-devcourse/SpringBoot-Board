package com.example.board.global.security.jwt;

import com.example.board.domain.member.entity.Member;
import com.example.board.domain.member.repository.MemberRepository;
import com.example.board.global.exception.BusinessException;
import com.example.board.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(jwtAuthenticationToken.getPrincipal()),
                jwtAuthenticationToken.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String principal, String credential) {
        try {
            Member member = findMember(principal, credential);
            List<GrantedAuthority> authorities = member.getAuthorities();

            String accessToken = generateToken(member.getEmail(), authorities, TokenType.ACCESS);

            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(
                    new JwtAuthentication(member.getEmail(), accessToken),
                    null,
                    authorities
            );
            authenticated.setDetails(member);
            return authenticated;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    // Token 발급
    public String generateToken(String username, List<GrantedAuthority> authorities, TokenType tokenType) {
        String[] roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return jwt.sign(Jwt.Claims.from(username, roles, tokenType));
    }

    public Member findMember(String username, String credentials) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.checkPassword(passwordEncoder, credentials);
        return member;
    }
}
