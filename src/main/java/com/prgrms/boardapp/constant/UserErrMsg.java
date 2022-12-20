package com.prgrms.boardapp.constant;

import static com.prgrms.boardapp.model.CommonEmbeddable.CREATED_BY_MAX_LENGTH;
import static com.prgrms.boardapp.model.User.*;

public enum UserErrMsg {
    NAME_LENGTH_ERR_MSG("이름의 최대길이는 " + NAME_MAX_LENGTH +" 자 입니다."),

    CREATED_LENGTH_ERR_MSG("취미의 최대길이는 " + CREATED_BY_MAX_LENGTH +" 자 입니다."),

    AGE_VALIDATE_ERR("나이는 음수가 될 수 없습니다");

    private final String message;

    UserErrMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
