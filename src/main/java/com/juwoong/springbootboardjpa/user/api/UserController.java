package com.juwoong.springbootboardjpa.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.juwoong.springbootboardjpa.user.api.model.UserRequest;
import com.juwoong.springbootboardjpa.user.application.UserService;
import com.juwoong.springbootboardjpa.user.application.model.UserDto;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserRequest.Create request) {
        UserDto user = userService.createUser(request.name(), request.age(), request.hobby());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.getUser(id);

        return ResponseEntity.ok(user);
    }

}
