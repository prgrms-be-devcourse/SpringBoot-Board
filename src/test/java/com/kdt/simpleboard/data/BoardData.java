package com.kdt.simpleboard.data;

import com.kdt.simpleboard.board.domain.Board;
import com.kdt.simpleboard.board.dto.BoardRequest;
import com.kdt.simpleboard.board.dto.BoardResponse;
import com.kdt.simpleboard.user.UserData;

import java.util.List;

import static com.kdt.simpleboard.board.dto.BoardRequest.*;
import static com.kdt.simpleboard.board.dto.BoardResponse.*;

public class BoardData {

    public static CreateBoardRequest createBoardRequest() {
        return new CreateBoardRequest(1L, "titleA", "contentA");
    }

    public static CreateBoardRequest createBoardRequest(Long userId) {
        return new CreateBoardRequest(userId, "titleA", "contentA");
    }

    public static ModifyBoardRequest modifyBoardRequest() {
        return new ModifyBoardRequest("titleAChanged", "contentAChanged");
    }

    public static CreateBoardResponse createBoardResponse() {
        return new CreateBoardResponse(1L);
    }

    public static FindBoardResponse findBoardResponse() {
        return new FindBoardResponse(1L, "titleA", "contentA");
    }

    public static Board board() {
        return Board.builder()
                .title("titleA")
                .content("contentA")
                .user(UserData.user())
                .build();
    }

    public static Board board(String title, String content) {
        return Board.builder()
                .title(title)
                .content(content)
                .user(UserData.user())
                .build();
    }

    public static List<Board> getBoards() {
        Board board1 = board();
        Board board2 = board("titleB", "contentB");

        return List.of(board1, board2);
    }
}
