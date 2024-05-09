package com.alle.api.domain.board.domain;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.global.domain.AbstractTimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board_favorite")
public class Favorite extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}
