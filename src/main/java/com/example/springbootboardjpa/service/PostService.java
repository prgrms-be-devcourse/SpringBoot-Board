package com.example.springbootboardjpa.service;

import com.example.springbootboardjpa.dto.PostDTO;
import com.example.springbootboardjpa.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostService {

    @Transactional
    PostDTO.Response findById(long id);

    @Transactional
    long save(PostDTO.Save postDTO);

    @Transactional
    void update(long id, String title, String contents);

    @Transactional
    Page<PostDTO.Response> findAll(Pageable pageable);
}
