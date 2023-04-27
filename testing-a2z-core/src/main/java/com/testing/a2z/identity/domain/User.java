package com.testing.a2z.identity.domain;

import java.util.Objects;
import java.util.UUID;

import com.testing.a2z.identity.domain.password.PasswordValidator;

public record User(UUID id,
                   String username,
                   String password) {

    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(username);
        PasswordValidator.validate(password);
    }

    // todo - maknuti validaciju u komandu, u domeni imati metodu za usporedbu sifre

}
