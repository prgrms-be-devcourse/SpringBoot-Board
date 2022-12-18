package com.prgrms.devcourse.springjpaboard.domain.user.exeception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

	private static final String MESSAGE = "존재하지 않는 User입니다.";

	public UserNotFoundException() {
		super(MESSAGE);
	}
}
