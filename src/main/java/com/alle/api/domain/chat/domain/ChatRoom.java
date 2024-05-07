package com.alle.api.domain.chat.domain;

import com.alle.api.domain.chat.constant.ChatRoomType;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity(name = "chat_room")
public class ChatRoom extends AbstractModifier {

    @Column
    private String title;

    @Column
    private ChatRoomType type;

    @Column
    private String lastMessage;
}
