package com.alle.api.domain.board.controller;

import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.service.BoardService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CustomUserDetail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/write")
    public Response<Void> write(
            @AuthenticationPrincipal CustomUserDetail userDetail
            , @RequestBody BoardWriteReq boardWriteReq) {

        boardService.write(userDetail, boardWriteReq);

        return Response.success(HttpStatus.OK, "write success");
    }

    @PostMapping("/update")
    public Response<Void> update(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardUpdateReq boardUpdateReq
    ) {
        boardService.update(userDetail, boardUpdateReq);

        return Response.success(HttpStatus.OK, "update success");

    }

    @DeleteMapping("/delete/{id}")
    public Response<Void> delete(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam("Id") Long id
            ) {
        //TODO:: 글 삭제시 비밀번호인증? 아니면 바로 삭제 가능?
//        boardService.delete(userDetail, id);
        return Response.success(HttpStatus.OK, "delete success");

    }
}
