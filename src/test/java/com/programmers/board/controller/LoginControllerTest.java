package com.programmers.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.board.controller.request.LoginRequest;
import com.programmers.board.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.programmers.board.constant.SessionConst.LOGIN_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureRestDocs
class LoginControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    LoginService loginService;

    @Nested
    @DisplayName("중첩: 로그인 요청")
    class LoginTest {
        LoginRequest request;

        @BeforeEach
        void setUp() {
            request = new LoginRequest("name");
        }

        @Test
        @DisplayName("성공(204)")
        void login() throws Exception {
            //given
            given(loginService.login(any())).willReturn(1L);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));

            //then
            resultActions.andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document("login",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(STRING).description("회원 이름")
                            )));
        }

        @Test
        @DisplayName("성공: 세션 검증")
        void login_session() throws Exception {
            //given
            given(loginService.login(any())).willReturn(1L);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));

            //then
            HttpSession session = resultActions.andDo(print())
                    .andExpect(status().isNoContent())
                    .andReturn().getRequest().getSession();
            assertThat(session).isNotNull();
            assertThat(session.getAttribute(LOGIN_USER_ID)).isEqualTo(1L);
        }

        @Test
        @DisplayName("예외: 사용자 이름 null")
        void login_ButNullName() throws Exception {
            //given
            String nullName = null;
            LoginRequest request = new LoginRequest(nullName);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));

            //then
            resultActions.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }

        @ParameterizedTest
        @CsvSource({
                "한글", "!@#$%", "12345"
        })
        @DisplayName("예외: 잘못된 형식의 사용자 이름")
        void login_ButInvalidName(String invalidName) throws Exception {
            //given
            LoginRequest request = new LoginRequest(invalidName);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));

            //then
            resultActions.andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }
    }

    @Nested
    @DisplayName("중첩: 로그아웃 요청")
    class LogoutTest {
        @Test
        @DisplayName("성공(204)")
        void logout() throws Exception {
            //given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(LOGIN_USER_ID, 1L);
            MockCookie jsessionid = new MockCookie("JSESSIONID", session.getId());

            //when
            ResultActions resultActions = mvc.perform(get("/api/v1/logout")
                    .session(session)
                    .cookie(jsessionid));

            //then
            resultActions.andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document("logout",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestCookies(
                                    cookieWithName("JSESSIONID").description("세션 쿠키")
                            )));
        }

        @Test
        @DisplayName("성공: 세션 초기화 검증")
        void logout_session() throws Exception {
            //given
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(LOGIN_USER_ID, 1L);

            //when
            ResultActions resultActions = mvc.perform(get("/api/v1/logout")
                    .session(session));

            //then
            HttpSession findSession = resultActions.andDo(print())
                    .andExpect(status().isNoContent())
                    .andReturn().getRequest().getSession();
            assertThat(findSession).isNotNull();
            assertThat(findSession.getAttribute(LOGIN_USER_ID)).isNull();
        }
    }
}