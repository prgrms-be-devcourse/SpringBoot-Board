package com.programmers.board.service;

import com.programmers.board.service.request.user.UserDeleteCommand;
import com.programmers.board.service.request.user.UserGetCommand;
import com.programmers.board.domain.User;
import com.programmers.board.service.response.UserResponse;
import com.programmers.board.service.request.user.UserCreateCommand;
import com.programmers.board.service.request.user.UserUpdateCommand;
import com.programmers.board.exception.AuthorizationException;
import com.programmers.board.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    User givenUser;

    @BeforeEach
    void setUp() {
        givenUser = new User("name", 20, "hobby");
    }

    @Nested
    @DisplayName("중첩: 회원 생성")
    class CreateUserTest {
        @Test
        @DisplayName("성공")
        void createUser() {
            //given
            String name = "name";
            int age = 20;
            String hobby = "취미";

            UserCreateCommand command = UserCreateCommand.of(name, age, hobby);

            //when
            userService.createUser(command);

            //then
            then(userRepository).should().save(any());
        }

        @Test
        @DisplayName("예외: 중복된 회원 이름")
        void createUser_ButNameDuplication() {
            //given
            String name = "name";
            User user = new User(name, 20, "hobby");
            UserCreateCommand command = UserCreateCommand.of(name, 20, "hobby");

            given(userRepository.findByName(any())).willReturn(Optional.of(user));

            //when
            //then
            assertThatThrownBy(() -> userService.createUser(command))
                    .isInstanceOf(DuplicateKeyException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "한글은 안됨", "thisIsOver30CharactersThenException"
        })
        @DisplayName("예외: 잘못된 이름 문자열")
        void createUser_ButInvalidName(String invalidName) {
            //given
            UserCreateCommand command = UserCreateCommand.of(invalidName, 20, "hobby");

            //when
            //then
            assertThatThrownBy(() -> userService.createUser(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "숫자는 안됨1", "thisIsNotContainsNumberButOverFiftyCharactersThenException"
        })
        @DisplayName("예외: 잘못된 취미 문자열")
        void createUser_ButInvalidHobby(String invalidHobby) {
            //given
            UserCreateCommand command = UserCreateCommand.of("name", 20, invalidHobby);

            //when
            //then
            assertThatThrownBy(() -> userService.createUser(command))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("성공: 회원 목록 조회")
    void findUsers() {
        //given
        int page = 0;
        int size = 1;
        PageRequest pageRequest = PageRequest.of(page, size);
        PageImpl<User> givenResult = new PageImpl<>(List.of(givenUser), pageRequest, 1);

        given(userRepository.findAll(any(PageRequest.class))).willReturn(givenResult);

        //when
        Page<UserResponse> users = userService.findUsers(pageRequest);

        //then
        assertThat(users.getContent()).hasSize(1);
    }

    @Nested
    @DisplayName("중첩: 회원 단건 조회")
    class FindUserTest {
        @Test
        @DisplayName("성공")
        void findUser() {
            //given
            Long userId = 1L;
            UserGetCommand command = UserGetCommand.of(userId);

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            UserResponse findUserDto = userService.findUser(command);

            //then
            assertThat(findUserDto.getName()).isEqualTo(givenUser.getName());
            assertThat(findUserDto.getAge()).isEqualTo(givenUser.getAge());
            assertThat(findUserDto.getHobby()).isEqualTo(givenUser.getHobby());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 회원")
        void findUser_ButEmpty() {
            //given
            Long userId = 1L;
            UserGetCommand command = UserGetCommand.of(userId);

            given(userRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> userService.findUser(command))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("중첩: 회원 수정")
    class UpdateUserTest {
        @Test
        @DisplayName("성공")
        void updateUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 1L;
            String updateName = "updateName";
            Integer updateAge = 22;
            String updateHobby = "updateHobby";
            UserUpdateCommand command = UserUpdateCommand.of(
                    userId, loginUserId,
                    updateName, updateAge, updateHobby
            );

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            userService.updateUser(command);

            //then
            assertThat(givenUser.getName()).isEqualTo(updateName);
            assertThat(givenUser.getAge()).isEqualTo(updateAge);
            assertThat(givenUser.getHobby()).isEqualTo(updateHobby);
        }

        @Test
        @DisplayName("예외: 업데이트 대상 회원과 로그인 회원 불일치")
        void updateUser_ButNotEqualUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 2L;
            UserUpdateCommand command = UserUpdateCommand.of(
                    userId, loginUserId,
                    null, null, null
            );

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            //then
            assertThatThrownBy(() -> userService.updateUser(command))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("중첩: 회원 삭제")
    class DeleteUserTest {
        @Test
        @DisplayName("성공")
        void deleteUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 1L;
            UserDeleteCommand command = UserDeleteCommand.of(userId, loginUserId);

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            userService.deleteUser(command);

            //then
            then(userRepository).should().delete(any());
        }

        @Test
        @DisplayName("예외: 업데이트 대상 회원과 로그인 회원 불일치")
        void deleteUser_ButNotEqualUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 2L;
            UserDeleteCommand command = UserDeleteCommand.of(userId, loginUserId);

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            //then
            assertThatThrownBy(() -> userService.deleteUser(command))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("중첩: 회원 이름 중복 검사")
    class IsDuplicatedUserNameTest {
        @Test
        @DisplayName("성공: 회원 중복O")
        void success_duplicate() {
            //given
            given(userRepository.findByName(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            boolean duplicated = userService.isDuplicatedUserName("name");

            //then
            assertThat(duplicated).isTrue();
        }

        @Test
        @DisplayName("성공: 회원 중복X")
        void success_notDuplicate() {
            //given
            given(userRepository.findByName(any())).willReturn(Optional.empty());

            //when
            boolean duplicated = userService.isDuplicatedUserName("name");

            //then
            assertThat(duplicated).isFalse();
        }
    }
}