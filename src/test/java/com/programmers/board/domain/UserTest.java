package com.programmers.board.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Nested
    @DisplayName("중첩: user 생성")
    class NewUserTest {
        @ParameterizedTest
        @CsvSource({
                "tester123", "tester", "THEONERING"
        })
        @DisplayName("성공: 유효한 이름")
        void user_ValidName(String validName) {
            //given
            //when
            User user = new User(validName, 20, "hobby");

            //then
            assertThat(user.getName()).isEqualTo(validName);
        }

        @ParameterizedTest
        @CsvSource({
                "tester", "절대반지", "THEONERING"
        })
        @DisplayName("성공: 유효한 취미")
        void user_ValidHobby(String validHobby) {
            //given
            //when
            User user = new User("name", 20, validHobby);

            //then
            assertThat(user.getHobby()).isEqualTo(validHobby);
        }

        @ParameterizedTest
        @CsvSource({
                "한글이름", "!@#$%%^"
        })
        @DisplayName("예외: 잘못된 형식의 사용자 이름")
        void user_ButInvalidName(String invalidName) {
            assertThatThrownBy(() -> new User(invalidName, 20, "hobby"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "number123", "한글123", "!@#$%"
        })
        @DisplayName("예외: 잘못된 형식의 사용자 취미")
        void user_ButInvalidHobby(String invalidHobby) {
            assertThatThrownBy(() -> new User("name", 20, invalidHobby))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 사용자 이름 null")
        void user_ButNameIsNull() {
            //given
            String nullName = null;

            //when
            //then
            assertThatThrownBy(() -> new User(nullName, 20, "hobby"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 사용자 취미 null")
        void user_ButHobbyIsNull() {
            //given
            String nullHobby = null;

            //when
            //then
            assertThatThrownBy(() -> new User("name", 20, nullHobby))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("중첩: user 업데이트")
    class UpdateUserTest {
        User givenUser;

        @BeforeEach
        void setUp() {
            givenUser = new User("name", 20, "hobby");
        }

        @ParameterizedTest
        @CsvSource({
                "한글이름", "!@#$%%^"
        })
        @DisplayName("예외: 잘못된 형식의 사용자 이름")
        void updateUser_ButInvalidName(String invalidName) {
            assertThatThrownBy(() -> givenUser.updateMember(invalidName, null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @CsvSource({
                "number123", "한글123", "!@#$%"
        })
        @DisplayName("예외: 잘못된 형식의 사용자 취미")
        void updateUser_ButInvalidHobby(String invalidHobby) {
            assertThatThrownBy(() -> givenUser.updateMember(null, null, invalidHobby))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}