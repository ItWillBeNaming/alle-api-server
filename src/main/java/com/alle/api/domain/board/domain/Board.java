package com.alle.api.domain.board.domain;

import com.alle.api.global.domain.AbstractTimeStamp;
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
public class Board extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;


    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    /**
     * Default: 초기화 메서드
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Favorite> favorites = new ArrayList<>();


    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;

    }


    public void increaseViewCount() {
        this.viewCount +=1;
    }

    // 연관 관계 편의 메서드
    public void addComment(BoardComment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

    public void removeComment(BoardComment comment) {
        comments.remove(comment);
        comment.setBoard(null);
    }

    public void addLike(Like like) {
        likes.add(like);
        like.setBoard(this);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setBoard(null);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }


}
