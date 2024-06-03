package com.alle.api.domain.board.domain;

import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
public class Board extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name="view_count", nullable = false)
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @OneToMany(mappedBy = "board")
    private List<BoardComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Favorite> favorites = new ArrayList<>();


    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;

    }


}
