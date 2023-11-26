package com.prgrms.board.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindPostsResponse {

    int count;

    List<FindPostResponse> findPostResponseList;
}
