package com.alle.api.domain.chat.domain;

import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;

@Entity(name = "chat_message")
public class ChatMessage extends AbstractModifier {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatRoom chatRoom;

    @Column
    private String content;
}
