package devcource.hihi.boardjpa.service;

import devcource.hihi.boardjpa.domain.Post;
import devcource.hihi.boardjpa.domain.User;
import devcource.hihi.boardjpa.dto.CursorResult;
import devcource.hihi.boardjpa.dto.post.CreateRequestDto;
import devcource.hihi.boardjpa.dto.post.ResponsePostDto;
import devcource.hihi.boardjpa.dto.post.UpdateRequestDto;
import devcource.hihi.boardjpa.repository.PostRepository;
import devcource.hihi.boardjpa.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository boardRepository, UserRepository userRepository) {
        this.postRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponsePostDto createPost(Long userId, CreateRequestDto postDto) {
        Post post = postDto.toEntity();

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("작성자는 null이 될 수 없습니다"));
        post.allocateUser(user);
        Post save = postRepository.save(post);

        return ResponsePostDto.toResponseDto(save);
    }

    @Transactional(readOnly = true)
    public ResponsePostDto findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id에 해당하는 post가 없습니다."));
        return ResponsePostDto.toResponseDto(post);
    }

    @Transactional
    public ResponsePostDto updatePost(Long id, UpdateRequestDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id에 해당하는 post가 없습니다."));
        post.changeTitle(dto.title());
        post.changeContent(dto.content());
        return ResponsePostDto.toResponseDto(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(Long cursorId, int size) {
        if (cursorId == null) {
            return postRepository.findFirstPage(size);
        }
        if (cursorId < 0) {
            throw new RuntimeException("커서 아이디는 음수가 될 수 없습니다.");
        }

        return postRepository.findByIdLessThanOrderByIdDesc(cursorId, size);
    }

    @Transactional(readOnly = true)
    public Boolean hasNext(Long id) {
        if (id == null) return false;
        return this.postRepository.existsByIdLessThan(id);
    }

}
