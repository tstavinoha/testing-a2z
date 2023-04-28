package com.testing.a2z.identity;

import java.util.UUID;

// todo - redoslijed: 1
public record User(UUID id,
                   String username,
                   String passwordHash) {

    public boolean passwordHashEquals(String passwordHash) {
        return this.passwordHash.equals(passwordHash);
    }

}
