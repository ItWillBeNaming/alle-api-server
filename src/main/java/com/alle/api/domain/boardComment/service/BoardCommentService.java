package com.alle.api.domain.boardComment.service;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.repository.BoardRepository;
import com.alle.api.domain.boardComment.domain.BoardComment;
import com.alle.api.domain.boardComment.dto.request.BoardChildCommentReq;
import com.alle.api.domain.boardComment.dto.request.BoardParentCommentReq;
import com.alle.api.domain.boardComment.dto.response.BoardCommentResponse;
import com.alle.api.domain.boardComment.repository.BoardCommentRepository;
import com.alle.api.domain.boardComment.repository.CustomBoardCommentRepositoryImpl;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.BoardException;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardCommentService {

    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final CustomBoardCommentRepositoryImpl customBoardCommentRepository;
    private final MemberRepository memberRepository;


    private BoardComment getBoardComment(Long parentId) {
        BoardComment parentComment = boardCommentRepository.findById(parentId).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARDCOMMENT)
        );
        return parentComment;
    }

    public void addParentComment(Long id, CustomUserDetail userDetail, BoardParentCommentReq boardCommentReq) {
        Member findMember = getMember(userDetail);
        Board board = getBoard(id);

        BoardComment boardComment = BoardComment.builder()
                .content(boardCommentReq.getComment())
                .member(findMember)
                .board(board)
                .build();

        boardComment.updateStatus(findMember);
        boardComment.setParentComment(boardComment);

        board.addComment(boardComment);
        boardRepository.save(board);
    }

    public void addChildComment(Long parentId, CustomUserDetail userDetail, BoardChildCommentReq boardChildCommentReq) {
        Member findMember = getMember(userDetail);
        BoardComment parentComment = getBoardComment(parentId);

        BoardComment childComment = BoardComment.builder()
                .content(boardChildCommentReq.getContent())
                .member(findMember)
                .board(parentComment.getBoard())
                .parentComment(parentComment)
                .build();

        childComment.updateStatus(findMember);

        parentComment.setChildComment(childComment);
        boardCommentRepository.save(childComment);
    }

    private Member getMember(CustomUserDetail userDetail) {
        return memberRepository.findByEmail(userDetail.getUsername()).orElseThrow(() ->
                new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
    }

    public Page<BoardCommentResponse> findComments(Long id, Pageable pageable) {
        List<BoardComment> comments = boardCommentRepository.findByBoardIdWithChildren(id);

        List<BoardCommentResponse> responses = comments.stream()
                .map(comment ->
                        new BoardCommentResponse(
                        comment.getId(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                        comment.getChildComments().stream().map(BoardComment::getId).collect(Collectors.toList()),
                        comment.getContent()
                ))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responses.size());
        Page<BoardCommentResponse> page = new PageImpl<>(responses.subList(start, end), pageable, responses.size());

        return page;
    }
}
