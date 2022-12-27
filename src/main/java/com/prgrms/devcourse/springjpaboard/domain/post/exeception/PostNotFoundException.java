package com.prgrms.devcourse.springjpaboard.domain.post.exeception;

import com.prgrms.devcourse.springjpaboard.global.error.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {

	private static final String MESSAGE = "존재하지 않는 Post입니다.";

	public PostNotFoundException() {
		super(MESSAGE);
	}
}
