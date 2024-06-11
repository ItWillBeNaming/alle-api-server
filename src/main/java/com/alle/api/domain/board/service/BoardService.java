package com.alle.api.domain.board.service;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.domain.BoardComment;
import com.alle.api.domain.board.dto.request.BoardChildCommentReq;
import com.alle.api.domain.board.dto.request.BoardParentCommentReq;
import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.repository.BoardCommentRepository;
import com.alle.api.domain.board.repository.BoardRepository;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.BoardException;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final MemberRepository memberRepository;

    public void write(CustomUserDetail userDetail, BoardWriteReq boardWriteReq) {
        Member findMember = getMember(userDetail);

        Board board = Board.builder()
                .title(boardWriteReq.getTitle())
                .content(boardWriteReq.getContent())
                .writer(findMember.getNickname())
                .build();

        board.updateTimeStamp();

        boardRepository.save(board);

    }


    public void update(CustomUserDetail userDetail, BoardUpdateReq boardUpdateReq) {
        Member findMember = getMember(userDetail);
        Board findBoard = getBoard(boardUpdateReq);
            if (validWriter(findBoard, findMember)) {
                findBoard.updateBoard(boardUpdateReq.getTitle(), boardUpdateReq.getContent());
                boardRepository.save(findBoard);
        } else  {
            throw new BoardException(ExceptionCode.NOT_MATCHED_WRITER);
        }
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
        boardCommentRepository.save(boardComment);
    }

    public void addChildComment(Long parentId, CustomUserDetail userDetail, BoardChildCommentReq boardChildCommentReq) {
        Member findMember = getMember(userDetail);
        BoardComment parentComment = boardCommentRepository.findById(parentId).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARDCOMMENT)
        );

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


    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
    }

    private Board getBoard(BoardUpdateReq boardUpdateReq) {
        return boardRepository.findById(boardUpdateReq.getId()).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
    }


    public Board findOne(Long id) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );

        if (validateBoard(board)) {
            board.increaseViewCount();
            boardRepository.save(board);
        }
            return board;
    }



    public List<Board> findAll() {

        List<Board> boards = boardRepository.findAll().stream().filter(
                board -> board.getIsDeleted().equals(false)
        ).collect(Collectors.toList());

        return boards;

    }


    /**
     *  글 수정시 동일한 작성자인지 유효성 검사
     * @return
     */
    private  boolean validWriter(Board findBoard, Member findMember) {
        return findBoard.getWriter().equals(findMember.getNickname());
    }


    /**
     * 회원 유효성 검사 (존재하는 회원인가)
     * @return
     */

    private Member getMember(CustomUserDetail userDetail) {
        return memberRepository.findByLoginId(userDetail.getUsername()).orElseThrow(() ->
                new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
        );
    }



    /**
     * 게시판 유효성 검사(삭제된 글인가)
     * @param board
     */
    private boolean validateBoard(Board board) {
        if(board.getIsDeleted()){
            throw new BoardException(ExceptionCode.INVALID_BOARD);
        }
        return true;
    }



}
