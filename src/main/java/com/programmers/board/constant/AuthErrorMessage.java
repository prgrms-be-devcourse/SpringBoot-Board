package com.programmers.board.constant;

public enum AuthErrorMessage implements ErrorMessage {
    NO_LOGIN("세션 정보가 없습니다"),
    NO_AUTHORIZATION("권한이 없습니다");

    private final String message;

    AuthErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
