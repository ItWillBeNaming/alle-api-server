package com.alle.api.domain.board.service.upperClass;


import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.dto.response.BoardResponse;
import com.alle.api.global.security.CustomUserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

     void write(CustomUserDetail customUserDetail, BoardWriteReq boardWriteReq);

     void update(CustomUserDetail customUserDetail, BoardUpdateReq boardUpdateReq);


    BoardResponse findOne(Long id);

    Page<BoardResponse> findAll(Pageable pageable);

    int like(Long id, CustomUserDetail userDetail);

}
