package com.programmers.board.controller.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResult<T> {
    private final List<T> data;
    private final boolean hasNext;
    private final boolean hasPrev;
    private final int totalPages;
    private final long totalElements;

    public PageResult(Page<T> data) {
        this.data = data.getContent();
        this.hasNext = data.hasNext();
        this.hasPrev = data.hasPrevious();
        this.totalPages = data.getTotalPages();
        this.totalElements = data.getTotalElements();
    }
}
