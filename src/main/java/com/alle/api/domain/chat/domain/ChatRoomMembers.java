package com.alle.api.domain.chat.domain;

import com.alle.api.domain.member.domain.Member;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "chat_room_members")
public class ChatRoomMembers extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column
    private LocalDateTime leftDate;
}
