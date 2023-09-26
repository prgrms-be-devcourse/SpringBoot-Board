package com.example.yiseul.controller;

import com.example.yiseul.dto.member.*;
import com.example.yiseul.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    private MemberResponseDto responseDto1;
    private MemberResponseDto responseDto2;
    private List<MemberResponseDto> responseDtos;
    private MemberCursorResponseDto cursorResponseDto;

    @BeforeEach
    void setUp(){
        responseDto1 = new MemberResponseDto(1L,"hihi", 22, "swim", "2023-07-19 17:00", "hihi");
        responseDto2 = new MemberResponseDto(2L,"ja", 24, "dance", "2023-07-30 18:00", "ja");
        responseDtos = Arrays.asList(responseDto1,responseDto2);
    }

    @Test
    @DisplayName("회원 가입에 성공한다.")
    void signUp() throws Exception {
        //given
        MemberCreateRequestDto createRequestDto = new MemberCreateRequestDto("hihi", 22, "hobby");

        given(memberService.createMember(createRequestDto))
                .willReturn(responseDto1);

        //when,then
        mvc.perform(post("/api/members")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseDto1)))

                .andDo(document("member-signup",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("age"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("hobby")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("age"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("hobby"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성시각"),
                                fieldWithPath("createdBy").type(JsonFieldType.STRING).description("생성자")
                        )
                ));
    }

    @Test
    @DisplayName("멤버 페이징 조회에 성공한다.")
    void getMembers() throws Exception {
        // given
        MemberPageResponseDto responseDto = new MemberPageResponseDto(responseDtos, 0, 2, 3, 4, true, false);

        Pageable pageable = PageRequest.of(0, 2);

        given(memberService.getMembers(pageable))
                .willReturn(responseDto);

        // when & then
        mvc.perform(MockMvcRequestBuilders.get("/api/members")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseDto)))

                .andDo(document("member-getMembersByPage",
                        responseFields(
                                fieldWithPath("memberResponseDto.[].memberId").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("memberResponseDto.[].name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("memberResponseDto.[].age").type(JsonFieldType.NUMBER).description("age"),
                                fieldWithPath("memberResponseDto.[].hobby").type(JsonFieldType.STRING).description("hobby"),
                                fieldWithPath("memberResponseDto.[].createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("memberResponseDto.[].createdBy").type(JsonFieldType.STRING).description("createdBy"),

                                fieldWithPath("pageNumber").type(JsonFieldType.NUMBER).description("pageNumber"),
                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("pageSize"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("totalPages"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("totalElements"),
                                fieldWithPath("isFirst").type(JsonFieldType.BOOLEAN).description("isFirst"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("isLast")
                )));
    }

    @Test
    @DisplayName("특정 멤버 조회에 성공한다.")
    void getMember() throws Exception {
        // given
        given(memberService.getMember(anyLong()))
                .willReturn(responseDto1);

        //when & then
        mvc.perform(get("/api/members/{memberId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseDto1)))

                .andDo(document("member-getMember",
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("age"),
                                fieldWithPath("hobby").type(JsonFieldType.STRING).description("hobby"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성시각"),
                                fieldWithPath("createdBy").type(JsonFieldType.STRING).description("생성자")
                        )
                ));
    }

    @Test
    @DisplayName("회원 정보 수정에 성공한다.")
    void updateMember() throws Exception {
        // given
        MemberUpdateRequestDto updateRequestDto = new MemberUpdateRequestDto("yiseul", 23, "swim");

        doNothing().when(memberService)
                .updateMember(anyLong(), any(MemberUpdateRequestDto.class));

        // when & then
        mvc.perform(patch("/api/members/{memberId}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                        .andExpect(status().isOk())

                        .andDo(document("member-update",
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("name"),
                                        fieldWithPath("age").type(JsonFieldType.NUMBER).description("age"),
                                        fieldWithPath("hobby").type(JsonFieldType.STRING).description("hobby")
                                )));
    }

    @Test
    @DisplayName("회원 정보 삭제에 성공한다.")
    void deleteMember() throws Exception {
        //given
        doNothing().when(memberService)
                .deleteMember(anyLong());

        // when & then
        mvc.perform(delete("/api/members/{memberId}",1))
                .andExpect(status().isOk())

                .andDo(document("member-delete"));
    }

    @Test
    @DisplayName("커서 방식 조회에 성공한다.")
    void getMembersCursor() throws Exception{
        //given
        Long cursorId = 1L;
        int size = 2;

        given(memberService.findMemberByCursor(cursorId,size)).willReturn(new MemberCursorResponseDto(responseDtos, 1L));

        //when & then
        mvc.perform(get("/api/members/cursor")
                .param("cursorId", String.valueOf(cursorId))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(new MemberCursorResponseDto(responseDtos, 1L))))

                .andDo(document("member-getMembersByCursor",
                        responseFields(
                                fieldWithPath("memberResponseDto.[].memberId").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("memberResponseDto.[].name").type(JsonFieldType.STRING).description("name"),
                                fieldWithPath("memberResponseDto.[].age").type(JsonFieldType.NUMBER).description("age"),
                                fieldWithPath("memberResponseDto.[].hobby").type(JsonFieldType.STRING).description("hobby"),
                                fieldWithPath("memberResponseDto.[].createdAt").type(JsonFieldType.STRING).description("createdAt"),
                                fieldWithPath("memberResponseDto.[].createdBy").type(JsonFieldType.STRING).description("createdBy"),

                                fieldWithPath("nextCursorId").type(JsonFieldType.NUMBER).description("nextCursorId")
                        )
                ));
    }

}
