package com.alle.api.domain.chat.domain;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.global.domain.AbstractTimeStamp;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "chat_message_status")
public class ChatMessageStatus extends AbstractTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatMessage chatMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean isRead;

    @Column
    private LocalDateTime readTime;
}
