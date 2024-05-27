package com.alle.api.domain.chat.domain;

import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.*;

@Entity(name = "chat_attachment")
public class ChatAttachment extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileExt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatMessage chatMessage;

}
