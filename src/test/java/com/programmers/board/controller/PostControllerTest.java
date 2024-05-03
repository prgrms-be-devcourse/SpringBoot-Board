package com.programmers.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmers.board.constant.SessionConst;
import com.programmers.board.domain.Post;
import com.programmers.board.domain.User;
import com.programmers.board.service.response.PostResponse;
import com.programmers.board.controller.request.PostCreateRequest;
import com.programmers.board.controller.request.PostUpdateRequest;
import com.programmers.board.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
class PostControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    PostService postService;

    PostResponse givenPost;
    MockHttpSession givenSession;

    @BeforeEach
    void setUp() {
        User user = new User("name", 20, "hobby");
        Post post = new Post("title", "content", user);
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(post, "id", 1L);
        givenPost = PostResponse.from(post);

        givenSession = new MockHttpSession();
        givenSession.setAttribute(SessionConst.LOGIN_USER_ID, 1L);
    }

    @Nested
    @DisplayName("중첩: post 목록 조회 호출")
    class FindPostsTest {
        @Test
        @DisplayName("성공(200)")
        void findPosts() throws Exception {
            //given
            int page = 0;
            int size = 1;
            PageRequest pageRequest = PageRequest.of(page, size);
            PageImpl<PostResponse> postDtos = new PageImpl<>(List.of(givenPost), pageRequest, 1);

            given(postService.findPosts(pageRequest)).willReturn(postDtos);

            //when
            ResultActions resultActions = mvc.perform(get("/api/v1/posts")
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size))
                    .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isOk())
                    .andDo(print())
                    .andDo(document("posts-get",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("page").description("페이지"),
                                    parameterWithName("size").description("사이즈")
                            ),
                            responseFields(
                                    beneathPath("data").withSubsectionId("data"),
                                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                    fieldWithPath("user").type(JsonFieldType.OBJECT).description("게시글 작성자"),
                                    fieldWithPath("user.userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                    fieldWithPath("user.name").type(JsonFieldType.STRING).description("작성자 이름"),
                                    fieldWithPath("user.age").type(JsonFieldType.NUMBER).description("작성자 나이"),
                                    fieldWithPath("user.hobby").type(JsonFieldType.STRING).description("작성자 취미")
                            )));
        }
    }

    @Nested
    @DisplayName("중첩: post 생성 호출")
    class CreatePostTest {
        @Test
        @DisplayName("성공(201)")
        void createPost() throws Exception {
            //given
            String title = "title";
            String content = "content";
            PostCreateRequest request = new PostCreateRequest(title, content);

            given(postService.createPost(any())).willReturn(1L);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/posts")
                    .session(givenSession)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isCreated())
                    .andDo(print())
                    .andDo(document("post-create",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문")
                            ),
                            responseFields(
                                    fieldWithPath("data").type(JsonFieldType.NUMBER).description("게시글 ID")
                            )));
        }

        @Test
        @DisplayName("실패(400): 필수값 공백")
        void createPost_ButValueIsBlank() throws Exception {
            //given
            String blankTitle = " ";
            String blankContent = " ";
            PostCreateRequest request = new PostCreateRequest(blankTitle, blankContent);

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print());

            //then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }

        @Test
        @DisplayName("실패(400): 잘못된 길이의 게시글 제목")
        void createPost_ButTitleOutOfRange() throws Exception {
            //given
            String titleOutOfRange = "a".repeat(101);
            PostCreateRequest request = new PostCreateRequest(titleOutOfRange, "content");

            //when
            ResultActions resultActions = mvc.perform(post("/api/v1/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print());

            //then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }
    }


    @Test
    @DisplayName("성공(200): post 단건 조회 호출")
    void findPost() throws Exception {
        //given
        Long postId = 1L;

        given(postService.findPost(any())).willReturn(givenPost);

        //when
        ResultActions resultActions = mvc.perform(get("/api/v1/posts/{postId}", postId)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        ),
                        responseFields(
                                beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                fieldWithPath("user").type(JsonFieldType.OBJECT).description("게시글 작성자"),
                                fieldWithPath("user.userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("작성자 이름"),
                                fieldWithPath("user.age").type(JsonFieldType.NUMBER).description("작성자 나이"),
                                fieldWithPath("user.hobby").type(JsonFieldType.STRING).description("작성자 취미")
                        )));
    }

    @Nested
    @DisplayName("중첩: post 업데이트 호출")
    class UpdatePostTest {
        @Test
        @DisplayName("성공(200)")
        void updatePost() throws Exception {
            //given
            Long postId = 1L;
            String title = "updateTitle";
            String content = "updateContent";
            PostUpdateRequest request = new PostUpdateRequest(title, content);

            //when
            ResultActions resultActions = mvc.perform(patch("/api/v1/posts/{postId}", postId)
                    .session(givenSession)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
                    .accept(MediaType.APPLICATION_JSON));

            //then
            resultActions.andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document("post-update",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("postId").description("게시글 ID")
                            ),
                            requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문")
                            )));
        }

        @Test
        @DisplayName("실패(400): 잘못된 길이의 게시글 제목")
        void updatePost_ButTitleOutOfRange() throws Exception {
            //given
            String titleOutOfRage = "a".repeat(101);
            PostUpdateRequest request = new PostUpdateRequest(titleOutOfRage, "content");

            //when
            ResultActions resultActions = mvc.perform(patch("/api/v1/posts/{postId}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print());

            //then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }

        @Test
        @DisplayName("실패(400): 잘못된 길이의 게시글 본문")
        void updatePost_ButContentOutOfRange() throws Exception {
            //given
            String contentOutOfRange = "";
            PostUpdateRequest request = new PostUpdateRequest("title", contentOutOfRange);

            //when
            ResultActions resultActions = mvc.perform(patch("/api/v1/posts/{postId}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andDo(print());

            //then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.detail").isString());
        }
    }

    @Test
    @DisplayName("성공(204): post 삭제 호출")
    void deletePost() throws Exception {
        //given
        Long postId = 1L;

        //when
        ResultActions resultActions = mvc.perform(delete("/api/v1/posts/{postId}", postId)
                .session(givenSession)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("post-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")
                        )));
    }
}