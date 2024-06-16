package com.alle.api;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockUser {
    long userId() default 4L;

    String loginId() default "test@test.com";

    String password() default "Leesunro12@";


    String[] authorities() default "ROLE_USER";
}
