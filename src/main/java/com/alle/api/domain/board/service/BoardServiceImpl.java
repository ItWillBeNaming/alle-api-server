package com.alle.api.domain.board.service;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.board.domain.BoardComment;
import com.alle.api.domain.board.dto.request.BoardChildCommentReq;
import com.alle.api.domain.board.dto.request.BoardParentCommentReq;
import com.alle.api.domain.board.dto.request.BoardUpdateReq;
import com.alle.api.domain.board.dto.request.BoardWriteReq;
import com.alle.api.domain.board.dto.response.BoardResponse;
import com.alle.api.domain.board.repository.BoardCommentRepository;
import com.alle.api.domain.board.repository.BoardRepository;
import com.alle.api.domain.board.repository.CustomBoardRepositoryImpl;
import com.alle.api.domain.board.service.upperClass.BoardService;
import com.alle.api.domain.boardLike.domain.BoardLike;
import com.alle.api.domain.boardLike.repository.BoardLikeRepository;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.repository.MemberRepository;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.exception.custom.BoardException;
import com.alle.api.global.exception.custom.MemberException;
import com.alle.api.global.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * redis를 사용하지 않고 좋아요 구현
 */

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CustomBoardRepositoryImpl customBoardRepository;


    @Override
    @Transactional
    public void write(CustomUserDetail userDetail, BoardWriteReq boardWriteReq) {
        Member findMember = getMember(userDetail);

        Board board = Board.builder()
                .title(boardWriteReq.getTitle())
                .content(boardWriteReq.getContent())
                .writer(findMember.getNickname())
                .likeCount(0)
                .build();

        board.updateTimeStamp();


        boardRepository.save(board);

    }


    @Override
    @Transactional
    public void update(CustomUserDetail userDetail, BoardUpdateReq boardUpdateReq) {
        Member findMember = getMember(userDetail);
        Board findBoard = getBoard(boardUpdateReq.getId());
        if (validWriter(findBoard, findMember)) {
            findBoard.updateBoard(boardUpdateReq.getTitle(), boardUpdateReq.getContent());
            boardRepository.save(findBoard);
        } else {
            throw new BoardException(ExceptionCode.NOT_MATCHED_WRITER);
        }
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public BoardResponse findOne(Long id) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
            board.increaseViewCount();
            boardRepository.save(board);

        BoardResponse boardResponse = BoardResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .build();


        return boardResponse;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<BoardResponse> findAll(Pageable pageable) {

        return customBoardRepository.BoardList(pageable);
    }



    @Override
    @Transactional
    public int like(Long id, CustomUserDetail userDetail) {
        Member findMember = getMember(userDetail);
        Board findBoard = getBoard(id);

        BoardLike findBoardLike = getBoardLike(findMember, findBoard);


        if (findBoardLike == null) {
            BoardLike boardLike = BoardLike.builder()
                    .board(findBoard)
                    .member(findMember)
                    .build();
            boardLike.updateStatus(findMember);

            boardLikeRepository.save(boardLike);
            findBoard.addLike(boardLike);
            findBoard.increaseLikeCount();
        } else {
            findBoard.decreaseLikeCount();
            findBoard.removeLike(findBoardLike);
            boardLikeRepository.delete(findBoardLike);
        }
        boardRepository.save(findBoard);

        return findBoard.getLikeCount();
    }

    private BoardComment getBoardComment(Long parentId) {
        BoardComment parentComment = boardCommentRepository.findById(parentId).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARDCOMMENT)
        );
        return parentComment;
    }


    private BoardLike getBoardLike(Member findMember, Board findBoard) {
        return boardLikeRepository.findByMemberAndBoard(findMember, findBoard).orElse(null);
    }


    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new BoardException(ExceptionCode.NOT_FOUND_BOARD)
        );
    }


    /**
     * 글 수정시 동일한 작성자인지 유효성 검사
     *
     * @return
     */
    private boolean validWriter(Board findBoard, Member findMember) {
        boolean isEqualToNickname = findBoard.getWriter().equals(findMember.getNickname());

        if (!isEqualToNickname) {
            throw new BoardException(ExceptionCode.NOT_MATCHED_WRITER);

        }
        return true;
    }


    /**
     * 회원 유효성 검사 (존재하는 회원인가)
     *
     * @return
     */

    private Member getMember(CustomUserDetail userDetail) {
        return memberRepository.findByLoginId(userDetail.getUsername()).orElseThrow(() ->
                new MemberException(ExceptionCode.NOT_FOUND_MEMBER)
        );
    }


    /**
     * 게시판 유효성 검사(삭제된 글인가)
     *
     * @param board
     */
    private boolean validateBoard(Board board) {
        if (board.getIsDeleted()) {
            throw new BoardException(ExceptionCode.INVALID_BOARD);
        }
        return true;
    }


}