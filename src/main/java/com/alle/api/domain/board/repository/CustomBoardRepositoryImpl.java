package com.alle.api.domain.board.repository;

import com.alle.api.domain.board.dto.response.BoardResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.alle.api.domain.board.domain.QBoard.*;


@RequiredArgsConstructor
@Repository
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardResponse> BoardList(Pageable peaPageable) {
        List<BoardResponse> boardResponse = queryFactory.select(Projections.constructor(
                        BoardResponse.class, board.id, board.title, board.content, board.viewCount, board.likeCount)
                )
                .from(board)
                .where(board.isDeleted.eq(false))
                .offset(peaPageable.getOffset())
                .limit(peaPageable.getPageSize())
                .fetch();
        Long count = queryFactory.select(board.count())
                .from(board)
                .fetchFirst();

        return new PageImpl<>(boardResponse, peaPageable,  count);
    }
}
