package com.testing.a2z.identity;

import java.util.UUID;

import com.testing.a2z.identity.password.Password;

// todo - redoslijed: 1
public record User(UUID id,
                   String username,
                   Password password) {

    public boolean verifyPassword(String plainPassword) {
        return password.verify(plainPassword);
    }

}
