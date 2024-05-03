package com.programmers.board.controller;

import com.programmers.board.constant.AuthErrorMessage;
import com.programmers.board.controller.request.LoginRequest;
import com.programmers.board.service.request.login.LoginCommand;
import com.programmers.board.exception.AuthenticationException;
import com.programmers.board.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.programmers.board.constant.SessionConst.LOGIN_USER_ID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest request,
                      HttpServletRequest httpServletRequest) {
        LoginCommand command = LoginCommand.from(request);
        Long userId = loginService.login(command);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(LOGIN_USER_ID, userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/logout")
    public void logout(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new AuthenticationException(AuthErrorMessage.NO_LOGIN.getMessage());
        }
        session.invalidate();
    }
}
