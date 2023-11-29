package com.example.board.domain.member.dto;

import org.hibernate.validator.constraints.Length;

public record MemberUpdateRequest(
        @Length(min = 2, max = 15, message = "이름은 최소 2자, 최대 15자로 설정할 수 있습니다.")
        String name,

        @Length(min = 2, max = 20, message = "취미는 최소 2자, 최대 20자로 설정할 수 있습니다.")
        String hobby
) {
}
