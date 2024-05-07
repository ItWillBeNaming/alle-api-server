package com.alle.api.domain.member.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;
}
