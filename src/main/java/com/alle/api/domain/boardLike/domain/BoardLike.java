package com.alle.api.domain.boardLike.domain;

import com.alle.api.domain.board.domain.Board;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Table(name = "board_like")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardLike extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonIgnore
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;


    public void setBoard(Board board) {
        this.board = board;
    }


}
