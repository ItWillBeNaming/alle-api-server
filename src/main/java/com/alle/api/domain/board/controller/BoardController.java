package com.alle.api.domain.board.controller;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.dto.request.BoardChildCommentReq;
import com.alle.api.domain.board.dto.request.BoardParentCommentReq;
import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.service.BoardService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController{

    private final BoardService boardService;

    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "작성 실패")
    })
    @PostMapping("/write")
    public Response<Void> write(
            @AuthenticationPrincipal CustomUserDetail userDetail,
             @RequestBody BoardWriteReq boardWriteReq) {



        boardService.write(userDetail, boardWriteReq);
        return Response.success(HttpStatus.OK, "write success");
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "수정 실패")
    })
    @PostMapping("/update")
    public Response<Void> update(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardUpdateReq boardUpdateReq) {
        boardService.update(userDetail, boardUpdateReq);
        return Response.success(HttpStatus.OK, "update success");
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "삭제 실패")
    })
    @DeleteMapping("/delete/{id}")
    public Response<Void> delete(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable("id") Long id) {
        // TODO: 글 삭제시 비밀번호인증? 아니면 바로 삭제 가능?
//        boardService.delete(userDetail, id);
        return Response.success(HttpStatus.OK, "delete success");
    }

    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/find/{id}")
    public Response<Board> findOne(
            @PathVariable("id") Long id) {
        Board findBoard = boardService.findOne(id);
        findBoard.increaseViewCount();
        return Response.success(HttpStatus.OK, "find success", findBoard);
    }

    @Operation(summary = "게시글 전체 조회", description = "모든 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })

    //TODO:: 페이징 필요
    @GetMapping("/find")
    public Response<List<Board>> findAll() {
        List<Board> BoardList = boardService.findAll();
        return Response.success(HttpStatus.OK, "find All Success", BoardList);
    }

    @PostMapping("/{id}/comments")
    public Response<Void> comment(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardParentCommentReq boardCommentReq
                                  ) {
        boardService.addParentComment(id, userDetail, boardCommentReq);


        return  Response.success(HttpStatus.OK, "Parent comment success");

    }

    @PostMapping("{parentId}/reply")
    public Response<Void> comment(
            @PathVariable("parentId") Long parentId,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardChildCommentReq boardChildCommentReq
    ) {
        boardService.addChildComment(parentId, userDetail,boardChildCommentReq);
        return Response.success(HttpStatus.OK,"Child comment success");
    }



}
