package com.programmers.board.docs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
@AutoConfigureRestDocs
public class DocumentControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("성공: page 목록 조회 공통 응답")
    void commonPageResult() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/page-result")
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("common-page-response",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                subsectionWithPath("data").type(JsonFieldType.VARIES).description("데이터"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부"),
                                fieldWithPath("hasPrev").type(JsonFieldType.BOOLEAN).description("이전 페이지 여부"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("모든 요소 수")
                        )));
    }

    @Test
    @DisplayName("성공: 단건 조회 공통 응답")
    void commonResult() throws Exception {
        //given
        //when
        ResultActions resultActions = mvc.perform(get("/result")
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("common-response",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                subsectionWithPath("data").type(JsonFieldType.VARIES).description("데이터")
                        )));
    }
}
