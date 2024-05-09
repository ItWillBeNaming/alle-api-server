package com.alle.api.domain.chat.domain;

import com.alle.api.domain.chat.constant.ChatRoomType;
import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;

@Entity(name = "chat_room")
public class ChatRoom extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private ChatRoomType type;

    @Column
    private String lastMessage;
}
