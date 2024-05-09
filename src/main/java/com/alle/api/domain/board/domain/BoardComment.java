package com.alle.api.domain.board.domain;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<BoardComment> childComments;


    // 댓글 또는 대댓글 업데이트 메서드
    public void updateComment(String content) {
        if (this.parentComment != null) {
            this.parentComment.content = content;
        } else {
            this.childComments.get(0).content = content;
        }
    }


}
