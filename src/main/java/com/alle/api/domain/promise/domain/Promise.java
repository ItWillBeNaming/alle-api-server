package com.alle.api.domain.promise.domain;

import com.alle.api.domain.board.domain.Board;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Promise")
public class Promise {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long promiseId;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "meeting_name", nullable = false)
    private String name;

    @Column(name = "meeting_date", nullable = false)
    private LocalDateTime meetingDate;

    @Column(name = "meeting_place", nullable = false)
    private String meetingPlace;

    @Column(name = "meeting_count", nullable = false)
    private int meetingCount;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_cancelled", nullable = false)
    private boolean cancelled = false;

    @OneToMany(mappedBy = "promise", cascade = CascadeType.ALL)
    private List<PromiseMember> promiseMembers = new ArrayList<>();


    public void updatePromise(String name , LocalDateTime meetingDate,String meetingPlace, String content,List<PromiseMember> promiseMembers) {
        if (!this.cancelled) {
            this.name = name;
            this.meetingDate = meetingDate;
            this.meetingPlace = meetingPlace;
            this.content = content;
            this.promiseMembers = promiseMembers;
        } else {
            // isCancelled가 true인 경우에는 업데이트를 수행하지 않음
            throw new IllegalStateException("이미 취소된 약속입니다");
        }
    }

    public void updateCancle() {
        this.cancelled = !this.cancelled;
    }




}
