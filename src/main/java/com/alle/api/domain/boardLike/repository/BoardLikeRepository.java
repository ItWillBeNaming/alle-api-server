package com.alle.api.domain.boardLike.repository;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.boardLike.domain.BoardLike;
import com.alle.api.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {



   Optional<BoardLike> findByMemberAndBoard(Member findMember, Board findBoard);

    boolean existsByMemberAndBoard(Member findMember, Board findBoard);
}
