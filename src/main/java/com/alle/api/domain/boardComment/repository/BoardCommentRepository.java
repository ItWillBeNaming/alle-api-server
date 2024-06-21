package com.alle.api.domain.boardComment.repository;

import com.alle.api.domain.board.dto.response.BoardResponse;
import com.alle.api.domain.boardComment.domain.BoardComment;
import com.alle.api.domain.boardComment.dto.response.BoardCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {


    Optional<List<BoardComment>> findByBoard(BoardResponse findBoard);

    @EntityGraph(value = "BoardComment.children", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT bc FROM BoardComment bc WHERE bc.board.id = :boardId order by bc.parentComment.id")
    List<BoardComment> findByBoardIdWithChildren(@Param("boardId") Long boardId);
}
