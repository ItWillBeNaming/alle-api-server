package com.alle.api.domain.board.controller;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.dto.request.BoardChildCommentReq;
import com.alle.api.domain.board.dto.request.BoardParentCommentReq;
import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.dto.response.BoardResponse;
import com.alle.api.domain.board.service.upperClass.BoardService;
import com.alle.api.global.domain.Response;
import com.alle.api.global.security.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardController {

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
    @PutMapping("/update")
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
    public Response<BoardResponse> findOne(
            @PathVariable("id") Long id) {
        BoardResponse findBoard = boardService.findOne(id);
        return Response.success(HttpStatus.OK, "find success", findBoard);
    }

    @Operation(summary = "게시글 전체 조회", description = "모든 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = {@Content(schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "400", description = "조회 실패")
    })

    @GetMapping("/find")
    public Response<Page<BoardResponse>> findAll(
            @Parameter(description = "페이지 시작 번호(0부터 시작)")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈(10부터 시작)")
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardResponse> boardList = boardService.findAll(pageable);
        return Response.success(HttpStatus.OK, "find All Success", boardList);
    }

    @PostMapping("/comments/{id}")
    public Response<Void> addParentComment(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardParentCommentReq boardCommentReq
    ) {
        boardService.addParentComment(id, userDetail, boardCommentReq);


        return Response.success(HttpStatus.OK, "Parent comment success");

    }

    @PostMapping("/comments/reply/{parentId}")
    public Response<Void> addChildComment(
            @PathVariable("parentId") Long parentId,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody BoardChildCommentReq boardChildCommentReq
    ) {
        boardService.addChildComment(parentId, userDetail, boardChildCommentReq);
        return Response.success(HttpStatus.OK, "Child comment success");
    }

    @Operation(summary = "게시글 좋아요 증가 또는 감소", description = "게시글의 좋아요를 증가하거나 감소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/like/{id}")
    public Response<Void> increaseOrDecreaseLike(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        long startTime = System.currentTimeMillis();
        log.info("start Time = {}", startTime);
        boardService.like(id, userDetail);

        long endTime = System.currentTimeMillis();
        log.info("during Time = {}", endTime - startTime);

        return Response.success(HttpStatus.OK, "like success");
    }


}
