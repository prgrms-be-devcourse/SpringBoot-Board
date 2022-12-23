package devcourse.board.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import devcourse.board.domain.member.MemberService;
import devcourse.board.domain.member.model.MemberRequest;
import devcourse.board.domain.post.PostService;
import devcourse.board.domain.post.model.PostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("게시글 생성")
    void call_createPost() throws Exception {
        // given
        Long savedMemberId = saveDummyMember("member");
        PostRequest.CreationDto creationDto =
                new PostRequest.CreationDto(savedMemberId, "title", "content");

        // when & then
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void call_getPost() throws Exception {
        // given
        Long createdPostId = createDummyPost("dummy-member", "title", "content");

        // when & then
        mockMvc.perform(get("/posts/{postId}", createdPostId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 전체 조회 (페이징)")
    void call_getPosts() throws Exception {
        // given
        createDummyPosts(20);

        // when & then
        mockMvc.perform(get("/posts")
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 업데이트")
    void call_updatePost() throws Exception {
        // given
        Long createdPostId = createDummyPost("dummy-member", "old-title", "old-content");
        PostRequest.UpdateDto updateDto = new PostRequest.UpdateDto("new-title", "new-content");

        // when & then
        mockMvc.perform(patch("/posts/{postId}", createdPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Long saveDummyMember(String name) {
        return memberService.join(new MemberRequest.JoinDto(name));
    }

    private Long createDummyPost(String memberName, String title, String content) {
        Long savedMemberId = saveDummyMember(memberName);
        PostRequest.CreationDto creationDto =
                new PostRequest.CreationDto(savedMemberId, title, content);

        return postService.createPost(creationDto);
    }

    private void createDummyPosts(int size) {
        for (int i = 1; i <= size; i++) {
            createDummyPost("member" + i, "title" + i, "content" + i);
        }
    }
}