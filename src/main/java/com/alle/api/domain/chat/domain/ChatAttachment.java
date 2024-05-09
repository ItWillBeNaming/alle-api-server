package com.alle.api.domain.chat.domain;

import com.alle.api.global.domain.AbstractModifier;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "chat_attachment")
public class ChatAttachment extends AbstractModifier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
