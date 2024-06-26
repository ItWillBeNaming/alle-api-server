package com.alle.api.domain.member.domain;

import com.alle.api.global.domain.AbstractTimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alarm")
public class Alarm extends AbstractTimeStamp {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Member sender;

    @ManyToOne(fetch = LAZY)
    private Member reciever;

    @Column(name = "alarm_content")
    private String content;

    @Column(name = "alarm_is_read")
    private boolean isRead;




}
