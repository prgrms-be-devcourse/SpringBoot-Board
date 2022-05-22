package prgrms.project.post.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prgrms.project.post.controller.response.DefaultApiResponse;
import prgrms.project.post.service.DefaultPage;
import prgrms.project.post.service.user.UserDto;
import prgrms.project.post.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<DefaultApiResponse<Long>> registerUser(@RequestBody @Validated UserDto userDto) {
        return ResponseEntity.ok(DefaultApiResponse.of(userService.registerUser(userDto)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DefaultApiResponse<UserDto>> searchUser(@PathVariable Long userId) {
        return ResponseEntity.ok(DefaultApiResponse.of(userService.searchById(userId)));
    }

    @GetMapping
    public ResponseEntity<DefaultApiResponse<DefaultPage<UserDto>>> searchAllUser(Pageable pageable) {
        return ResponseEntity.ok(DefaultApiResponse.of(userService.searchAll(pageable)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<DefaultApiResponse<Long>> updateUser(@PathVariable Long userId, @RequestBody @Validated UserDto userDto) {
        return ResponseEntity.ok(DefaultApiResponse.of(userService.updateUser(userId, userDto)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DefaultApiResponse<Boolean>> deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);

        return ResponseEntity.ok(DefaultApiResponse.of(true));
    }
}
