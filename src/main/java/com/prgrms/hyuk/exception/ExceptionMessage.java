package com.prgrms.hyuk.exception;

public enum ExceptionMessage {
    INVALID_NAME_FORMAT_EXP_MSG("이름은 1글자 이상 30글자 이하 입니다."),
    AGE_OUT_OF_RANGE_EXP_MSG("나이는 1이상 100이하 입니다."),
    INVALID_TITLE_FORMAT_EXP_MSG("제목은 10글자 이상 50글자 이하입니다."),
    INVALID_CONTENT_FORMAT_EXP_MSG("컨텐츠는 1글자 이상 작성해 주세요."),
    INVALID_POST_ID_EXP_MSG("유효하지 않은 게시글 id 입니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
