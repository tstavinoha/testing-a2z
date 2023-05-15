package com.testing.a2z.identity.password.hashed;

import com.testing.a2z.identity.password.validation.PasswordValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HashedPasswordFactory {

    private final SaltGenerator saltGenerator;
    private final Hasher hasher;

    public HashedPassword create(String plainPassword) {
        PasswordValidator.validate(plainPassword);

        var salt = saltGenerator.generate();
        var saltedPassword = HashedPassword.salten(plainPassword, salt);
        var hashedPassword = hasher.hash(saltedPassword);
        return new HashedPassword(salt, hashedPassword, hasher);
    }


    public HashedPassword create(String salt, String hashedPassword) {
        return new HashedPassword(salt, hashedPassword, hasher);
    }

}
