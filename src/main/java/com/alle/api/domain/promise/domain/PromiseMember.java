package com.alle.api.domain.promise.domain;

import com.alle.api.domain.member.domain.Member;
import jakarta.persistence.*;

import lombok.*;

import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Promise_Member")
public class PromiseMember {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "promise_id", nullable = false)
    private Promise promise;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}

