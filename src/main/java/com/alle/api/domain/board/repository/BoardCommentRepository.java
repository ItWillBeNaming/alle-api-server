package com.alle.api.domain.board.repository;

import com.alle.api.domain.board.domain.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {


}
