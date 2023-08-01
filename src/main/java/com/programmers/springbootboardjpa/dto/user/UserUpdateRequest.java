package com.programmers.springbootboardjpa.dto.user;

import static com.programmers.springbootboardjpa.global.validate.ValidationGroups.NotBlankGroup;
import static com.programmers.springbootboardjpa.global.validate.ValidationGroups.NotNullGroup;
import static com.programmers.springbootboardjpa.global.validate.ValidationGroups.SizeCheckGroup;

import com.programmers.springbootboardjpa.domain.user.Hobby;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @NotBlank(message = "사용자 이름은 Null이거나, 공백 또는 값이 없을 수 없습니다.", groups = NotBlankGroup.class)
    @Size(min = 2, max = 30, message = "사용자 이름은 2글자 이상, 30글자 미만이어야합니다.", groups = SizeCheckGroup.class)
    private final String name;

    @NotNull(message = "취미는 값이 Null일 수 없으며 목록에 있는 값이어야합니다.", groups = NotNullGroup.class)
    private final Hobby hobby;

    public UserUpdateRequest(String name, Hobby hobby) {
        this.name = name;
        this.hobby = hobby;
    }

}
