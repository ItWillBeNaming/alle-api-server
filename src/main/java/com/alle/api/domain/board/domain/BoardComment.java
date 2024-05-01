package com.alle.api.domain.board.domain;
import com.alle.api.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Board_Comment")
public class BoardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<BoardComment> childComments;

    @Column(name = "abstractModified")
    private String abstractModified;






    // 댓글 또는 대댓글 업데이트 메서드
    public void updateComment(String content) {
        if (this.parentComment != null) {
            this.parentComment.content = content;
        } else {
            this.childComments.get(0).content = content;
        }
    }


}
