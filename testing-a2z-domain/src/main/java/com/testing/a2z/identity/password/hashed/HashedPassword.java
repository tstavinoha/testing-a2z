package com.testing.a2z.identity.password.hashed;

import java.util.UUID;

import com.testing.a2z.identity.password.Password;
import lombok.Value;

@Value
public class HashedPassword implements Password {

    String salt;
    String hash;
    Hasher hasher;

    HashedPassword(String salt, String hash, Hasher hasher) {
        this.salt = salt;
        this.hash = hash;
        this.hasher = hasher;
    }

    @Override
    public boolean verify(String plainPassword) {
        return hasher.hash(salten(plainPassword, salt)).equals(hash);
    }

    // NOTE: This static method factory is not pure - it will return different results when repeatedly invoked with same arguments.
    // It is harder to test it and the assertions are less precise. Because we can't mock it, changes to the implementation in the future
    // may cause the need to alter a larger amount of tests than neccessary.
    @Deprecated
    public static HashedPassword create(String plainPassword, Hasher hasher) {
        var salt = UUID.randomUUID().toString().substring(0, 4);
        var hash = hasher.hash(salten(plainPassword, salt));
        return new HashedPassword(salt, hash, hasher);
    }

    // NOTE: This method is a pure function, so it does not cause any problems in tests.
    // A pure function is a function that, given the same input, will always return the same output
    // and does not have any observable side effect.
    static String salten(String plainPassword, String salt) {
        return plainPassword + salt;
    }

}
