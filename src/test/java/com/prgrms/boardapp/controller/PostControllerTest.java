package com.prgrms.boardapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.boardapp.dto.PostRequest;
import com.prgrms.boardapp.dto.PostResponse;
import com.prgrms.boardapp.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.prgrms.boardapp.common.PostCreateUtil.createPostRequest;
import static com.prgrms.boardapp.common.PostCreateUtil.createPostResponseWithId;
import static com.prgrms.boardapp.controller.DocumentInfo.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    PostRequest postRequest = createPostRequest();
    Long savedPostId = 1L;
    PostResponse postResponse = createPostResponseWithId(savedPostId);

    @Test
    @DisplayName("새로운 Post를 생성하는 API 호출")
    void testSave() throws Exception {
        given(postService.save(postRequest)).willReturn(savedPostId);

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(savedPostId))
                .andExpect(redirectedUrl("/posts/" + savedPostId))
                .andDo(print())
                .andDo(document("post-save",
                        requestFields(
                                getPostRequestFieldDescriptors()
                        ),
                        responseFields(
                                fieldWithPath(POST_ID.getField()).type(JsonFieldType.NUMBER).description(POST_ID.getDescription())
                        )
                ));
    }

    @Test
    @DisplayName("postId를 pathVariable로 API 조회할 수 있다.")
    void testFindById() throws Exception {
        given(postService.findById(savedPostId)).willReturn(postResponse);

        mockMvc.perform(get("/posts/{postId}", savedPostId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-findById",
                        responseFields(
                                getPostResponseFieldDescriptors()
                        )
                ));
    }

    @Test
    @DisplayName("페이징 처리하여 모든 데이터를 조회할 수 있다.")
    void testFindAllWithPaging() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 2);
        List<PostResponse> posts = List.of(
                createPostResponseWithId(1L),
                createPostResponseWithId(2L)
        );

        Page<PostResponse> pageResponse = new PageImpl<>(posts);
        given(postService.findAll(pageRequest)).willReturn(pageResponse);

        mockMvc.perform(get("/posts")
                .param("page", String.valueOf(pageRequest.getPageNumber()))
                .param("size", String.valueOf(pageRequest.getPageSize()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-findAll",
                        responseFields()
                                .andWithPrefix(CONTENT_ARRAY.getField(), getPostResponseFieldDescriptors())
                                .and(getPageableDescriptors())
                ));
    }

    @Test
    @DisplayName("존재하지 않는 아이디는 404를 반환")
    void testFindById404() throws Exception {
        given(postService.findById(savedPostId)).willThrow(new EntityNotFoundException("Throws EntityNotFoundException Message"));

        mockMvc.perform(get("/posts/{postId}", savedPostId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("서버 장애의 경우 500을 반환")
    void testFindById500() throws Exception {
        given(postService.findById(savedPostId)).willThrow(new RuntimeException("Throws RuntimeException Message"));

        mockMvc.perform(get("/posts/{postId}", savedPostId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("정상적으로 update 할 수 있다.")
    void testUpdate() throws Exception {
        PostRequest updatePostRequest = PostRequest.builder()
                .content("update-content")
                .title("update-title")
                .build();

        mockMvc.perform(patch("/posts/{postId}", savedPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePostRequest))
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("post-update",
                        requestFields(
                                getPostRequestFieldDescriptors()
                        )
                ));
    }

}