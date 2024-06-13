package com.alle.api.domain.board.repository;

import com.alle.api.domain.board.dto.response.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoardRepository {

    Page<BoardResponse> BoardList(Pageable peaPageable);
}
