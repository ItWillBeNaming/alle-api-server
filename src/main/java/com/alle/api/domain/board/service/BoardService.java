package com.alle.api.domain.board.service;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.repository.BoardRepository;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.exception.AlleException;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.BoardException;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void write(CustomUserDetail userDetail, BoardWriteReq boardWriteReq) {
        Member findMember = getMember(userDetail);

        Board board = Board.builder()
                .title(boardWriteReq.getTitle())
                .content(boardWriteReq.getContent())
                .writer(findMember.getNickname())
                .build();

        boardRepository.save(board);

    }


    public void update(CustomUserDetail userDetail, BoardUpdateReq boardUpdateReq) {
        Member findMember = getMember(userDetail);
        Board findBoard = getBoard(boardUpdateReq);
            if (validWriter(findBoard, findMember)) {
                findBoard.updateBoard(boardUpdateReq.getTitle(), boardUpdateReq.getContent());
        } else  {
            throw new BoardException(ExceptionCode.NOT_MATCHED_WRITER);
        }
    }

    private  boolean validWriter(Board findBoard, Member findMember) {
        return findBoard.getWriter().equals(findMember.getNickname())?true:false;
    }

    private Board getBoard(BoardUpdateReq boardUpdateReq) {
        Board findBoard = boardRepository.findById(boardUpdateReq.getId()).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
        return findBoard;
    }


    private Member getMember(CustomUserDetail userDetail) {
        return memberRepository.findByLoginId(userDetail.getName()).orElseThrow(() ->
                new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
        );
    }


}
