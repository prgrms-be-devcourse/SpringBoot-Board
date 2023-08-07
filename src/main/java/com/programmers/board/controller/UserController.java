package com.programmers.board.controller;

import com.programmers.board.constant.AuthConst;
import com.programmers.board.constant.AuthErrorMessage;
import com.programmers.board.controller.response.PageResult;
import com.programmers.board.controller.response.Result;
import com.programmers.board.dto.UserDto;
import com.programmers.board.dto.request.UserCreateRequest;
import com.programmers.board.dto.request.UserUpdateRequest;
import com.programmers.board.dto.request.UsersGetRequest;
import com.programmers.board.dto.service.*;
import com.programmers.board.exception.AuthenticationException;
import com.programmers.board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PageResult<UserDto> findUsers(@ModelAttribute @Valid UsersGetRequest request) {
        UsersGetCommand command = UsersGetCommand.from(request);
        Page<UserDto> users = userService.findUsers(command);
        return new PageResult<>(users);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Result<Long> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserCreateCommand command = UserCreateCommand.from(request);
        Long userId = userService.createUser(command);
        return new Result<>(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public Result<UserDto> findUser(@PathVariable("userId") Long userId) {
        UserGetCommand command = UserGetCommand.of(userId);
        UserDto user = userService.findUser(command);
        return new Result<>(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{userId}")
    public void updateUser(@PathVariable("userId") Long userId,
                           @SessionAttribute(name = AuthConst.LOGIN_USER_ID, required = false) Long loginUserId,
                           @RequestBody @Valid UserUpdateRequest request) {
        checkLogin(loginUserId);
        UserUpdateCommand command = UserUpdateCommand.of(userId, loginUserId, request);
        userService.updateUser(command);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId,
                           @SessionAttribute(name = AuthConst.LOGIN_USER_ID, required = false) Long loginUserId) {
        checkLogin(loginUserId);
        UserDeleteCommand command = UserDeleteCommand.of(userId, loginUserId);
        userService.deleteUser(command);
    }

    private void checkLogin(Long loginUserId) {
        if (loginUserId == null) {
            throw new AuthenticationException(AuthErrorMessage.NO_LOGIN.getMessage());
        }
    }
}
