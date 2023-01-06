package com.prgrms.springbootboardjpa.post.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.springbootboardjpa.post.application.PostService;
import com.prgrms.springbootboardjpa.post.dto.PostInsertRequest;
import com.prgrms.springbootboardjpa.post.dto.PostResponse;
import com.prgrms.springbootboardjpa.post.dto.PostUpdateRequest;
import com.prgrms.springbootboardjpa.member.domain.Member;
import com.prgrms.springbootboardjpa.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureRestDocs
@AutoConfigureMockMvc
//@WebMvcTest(PostRestController.class)
@SpringBootTest
class PostRestControllerTest {

    @Autowired
    PostRestController postRestController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    private Member tmpMember;
    private PostInsertRequest postInsertRequest;
    private PostResponse postResponse;

    @BeforeEach
    void beforeSetup() {
        tmpMember = new Member("email1", "password1", "name1", 10, "hobby1");
        memberRepository.save(tmpMember);
        postInsertRequest = new PostInsertRequest("title1", "content1");
        postResponse = postService.save(postInsertRequest, tmpMember);
    }

    @Test
    void savePostTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postInsertRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("Post-Save",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("createdBy").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("글 본문 내용")),
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("글 본문 내용"),
                                        fieldWithPath("createdBy").type(JsonFieldType.NUMBER).description("작성자 식별자"))

                        )
                );
    }

    @Test
    void findOnePostTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/posts/{postId}", postResponse.getPostId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("Post-FindById",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("글 본문 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성날짜"),
                                        fieldWithPath("createdBy").type(JsonFieldType.NUMBER).description("작성자 식별자")),
                                pathParameters(
                                        parameterWithName("postId").description("글 식별자")
                                )
                        )
                );
    }

    @Test
    void findOnePageTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/{pageNumber}", 0))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("Post-FindOnePage",
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("allContent").type(JsonFieldType.NUMBER).description("현재 페이지의 데이터 수"),
                                        fieldWithPath("allPage").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("pagePosts.[].postId").type(JsonFieldType.NUMBER).description("페이지내 각 글의 식별자"),
                                        fieldWithPath("pagePosts.[].title").type(JsonFieldType.STRING).description("페이지내 각 글의 제목"),
                                        fieldWithPath("pagePosts.[].content").type(JsonFieldType.STRING).description("페이지내 각 글의 내용"),
                                        fieldWithPath("pagePosts.[].createdAt").type(JsonFieldType.STRING).description("페이지내 각 글의 생성날짜"),
                                        fieldWithPath("pagePosts.[].createdBy").type(JsonFieldType.NUMBER).description("페이지내 각 글의 작성자 식별자")),
                                pathParameters(
                                        parameterWithName("pageNumber").description("페이지 번호")
                                )
                        )
                );
    }

    @Test
    void updatePostTest() throws Exception {
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(postResponse.getPostId(), tmpMember.getMemberId(), "updateTitle", "updateContent");
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("Post-Update",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("글 본문 내용")),
                                responseFields(
                                        fieldWithPath("postId").type(JsonFieldType.NUMBER).description("글 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("글 본문 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성날짜"),
                                        fieldWithPath("createdBy").type(JsonFieldType.NUMBER).description("작성자 식별자"))
                        )
                );

    }

}