package com.programmers.board.docs;

import com.programmers.board.controller.response.PageResult;
import com.programmers.board.controller.response.Result;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DocumentController {
    @GetMapping("/page-result")
    public PageResult<String> getPageResult() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        PageImpl<String> page = new PageImpl<>(List.of("데이터"), pageRequest, 1);
        return new PageResult<>(page);
    }

    @GetMapping("/result")
    public Result<String> getResult() {
        return new Result<>("데이터");
    }
}
