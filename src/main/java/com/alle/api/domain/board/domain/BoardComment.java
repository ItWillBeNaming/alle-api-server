package com.alle.api.domain.board.domain;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board_comment")
public class BoardComment extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardComment> childComments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    @JsonIgnore
    private Member member;



    // 연관 관계 편의 메서드
    public void setChildComment(BoardComment child) {
        childComments.add(child);
        child.setParentComment(this);
    }

    public void removeChildComment(BoardComment child) {
        childComments.remove(child);
        child.setParentComment(null);
    }

    public void setParentComment(BoardComment parentComment) {
        this.parentComment = parentComment;
    }

    //TODO:: 부모댓글이 삭제된다면 하위 댓글도 다 같이 삭제??
    public void removeParentComment(BoardComment parentComment) {
        this.parentComment = null;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


}
