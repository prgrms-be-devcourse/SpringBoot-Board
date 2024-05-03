package com.programmers.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.board.exception.AuthenticationException;
import com.programmers.board.exception.AuthorizationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalControllerAdviceTest.ExceptionController.class)
@Import(GlobalControllerAdviceTest.ExceptionController.class)
@AutoConfigureRestDocs
class GlobalControllerAdviceTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @RestController
    static class ExceptionController {
        @GetMapping("/no-such")
        public void noSuch() {
            throw new NoSuchElementException("no such element ex");
        }

        @GetMapping("/method-argument-not-valid")
        public void notValid(@ModelAttribute @Valid TestRequest request) {

        }

        @GetMapping("/illegal-argument")
        public void illegalArgument() {
            throw new IllegalArgumentException("input is invalid");
        }

        @GetMapping("/authorization")
        public void authorization() {
            throw new AuthorizationException("no auth");
        }

        @GetMapping("/authentication")
        public void authentication() {
            throw new AuthenticationException("no login");
        }

        @GetMapping("/duplicate")
        public void duplicate() {
            throw new DuplicateKeyException("duplicate");
        }

        @GetMapping("/ex")
        public void ex() {
            throw new RuntimeException("unexpected ex");
        }
    }

    static class TestRequest {
        @PositiveOrZero
        private final int age;

        public TestRequest(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    @DisplayName("성공(404): NoSuchElementException 예외 처리")
    void noSuchElementExHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/no-such"));

        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("ex-no-such-element",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(400): MethodArgumentNotValidException 예외 처리")
    void methodArgumentNotValidExHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/method-argument-not-valid")
                .param("age", "-1"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("ex-method-argument-not-valid",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(403): AuthorizationException 예외 처리")
    void authorizationException() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/authorization"));

        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(document("ex-authorization",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(400): IllegalArgumentException 예외 처리")
    void illegalArgumentExHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/illegal-argument"));

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("ex-illegal-argument",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(401): AuthenticationException 예외 처리")
    void authenticationExHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/authentication"));

        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("ex-authentication",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(409): DuplicateKeyException 예외 처리")
    void duplicateKeyExHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/duplicate"));

        //then
        resultActions.andExpect(status().isConflict())
                .andDo(print())
                .andDo(document("ex-duplicate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }

    @Test
    @DisplayName("성공(500): Exception 예외 처리")
    void exHandle() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/ex"));

        //then
        resultActions.andExpect(status().isInternalServerError())
                .andDo(print())
                .andDo(document("ex",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("type").type(JsonFieldType.STRING).description("문제 설명 문서"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("문제 요약"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("문제 상세 설명"),
                                fieldWithPath("instance").type(JsonFieldType.STRING).description("문제 발생 엔드포인트")
                        )));
    }
}