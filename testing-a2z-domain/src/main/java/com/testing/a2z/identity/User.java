package com.testing.a2z.identity;

import java.util.UUID;

import com.testing.a2z.identity.password.HashedPassword;

// todo - redoslijed: 1
public record User(UUID id,
                   String username,
                   HashedPassword hashedPassword) { // todo - maybe Password interface

    public boolean verifyPassword(String plainPassword) {
        return hashedPassword.verify(plainPassword);
    }

}
