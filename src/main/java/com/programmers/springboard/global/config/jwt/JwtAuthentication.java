package com.programmers.springboard.global.config.jwt;

import lombok.Getter;

@Getter
public class JwtAuthentication {
	private final String token;
	private final String username;

	JwtAuthentication(String token, String username) {
		validationToken(token);
		validationUsername(username);
		this.token = token;
		this.username = username;
	}

	private void validationToken(String token) {
		if (token.isEmpty()) {
			throw new IllegalArgumentException("JWT 토큰은 필수 입력 값입니다.");
		}
	}

	private void validationUsername(String username) {
		if (username.isEmpty()) {
			throw new IllegalArgumentException("아이디는 필수 입력 값입니다.");
		}
	}

}
