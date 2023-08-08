package com.programmers.heheboard.global.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
	private int statusCode;
	private T data;

	public ApiResponse(int statusCode, T data) {
		this.statusCode = statusCode;
		this.data = data;
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(200, data);
	}

	public static <T> ApiResponse<T> fail(int statusCode, T data) {
		return new ApiResponse<>(statusCode, data);
	}
}
