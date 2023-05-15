package com.testing.a2z.identity;

import com.testing.a2z.identity.password.HashedPasswordFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFactory {

    private UserIdGenerator userIdGenerator;
    private HashedPasswordFactory hashedPasswordFactory;

    public User createUser(String username, String password) {
        var userId = userIdGenerator.generate();
        var hashedPassword = hashedPasswordFactory.create(password);

        return new User(userId, username, hashedPassword);
    }

}
