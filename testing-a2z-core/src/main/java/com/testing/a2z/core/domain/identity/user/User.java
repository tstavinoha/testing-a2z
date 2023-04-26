package com.testing.a2z.core.domain.identity.user;

import java.util.Objects;
import java.util.UUID;

import com.testing.a2z.core.domain.identity.user.password.PasswordValidator;

public record User(UUID id,
                   String username,
                   String password) {

    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        PasswordValidator.validate(password);
    }

}
