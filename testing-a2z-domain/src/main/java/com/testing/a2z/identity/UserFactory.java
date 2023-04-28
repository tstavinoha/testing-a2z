package com.testing.a2z.identity;

import com.testing.a2z.identity.password.PasswordHasher;
import com.testing.a2z.identity.password.validation.PasswordValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFactory {

    private UserIdGenerator userIdGenerator;
    private PasswordHasher passwordHasher;

    public User createUser(String username, String password) {
        PasswordValidator.validate(password);

        var userId = userIdGenerator.generate();
        var passwordHash = passwordHasher.hash(password);

        return new User(userId, username, passwordHash);
    }

}
