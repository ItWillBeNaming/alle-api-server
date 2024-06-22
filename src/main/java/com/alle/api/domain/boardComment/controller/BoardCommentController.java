package com.alle.api.domain.boardComment.controller;

import com.alle.api.domain.boardComment.dto.request.BoardChildCommentReq;
import com.alle.api.domain.boardComment.dto.request.BoardParentCommentReq;
import com.alle.api.domain.boardComment.dto.response.BoardCommentResponse;
import com.alle.api.domain.boardComment.service.BoardCommentService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @GetMapping("/{id}")
    public Response<Page<BoardCommentResponse>> findComments(
            @PathVariable("id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<BoardCommentResponse> findComments = boardCommentService.findComments(id, pageable);


        return Response.success(HttpStatus.OK, "find All Success", findComments);


    }

    @PostMapping("/{id}")
    public Response<Void> addParentComment(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardParentCommentReq boardCommentReq
    ) {
        boardCommentService.addParentComment(id, userDetail, boardCommentReq);


        return Response.success(HttpStatus.OK, "Parent comment success");

    }

    @PostMapping("/reply/{parentId}")
    public Response<Void> addChildComment(
            @PathVariable("parentId") Long parentId,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardChildCommentReq boardChildCommentReq
    ) {
        boardCommentService.addChildComment(parentId, userDetail, boardChildCommentReq);
        return Response.success(HttpStatus.OK, "Child comment success");
    }

}
