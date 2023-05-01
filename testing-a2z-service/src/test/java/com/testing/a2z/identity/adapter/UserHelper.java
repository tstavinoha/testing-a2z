package com.testing.a2z.identity.adapter;

import java.util.UUID;

import com.testing.a2z.identity.User;
import com.testing.a2z.identity.adapter.out.persistence.UserPersistenceAdapter;
import com.testing.a2z.identity.password.PasswordHasher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserHelper {

    private final UserPersistenceAdapter userPersistenceAdapter;
    private final PasswordHasher passwordHasher;

    public User givenUser(String username, String password) {
        var id = UUID.randomUUID();
        var passwordHash = passwordHasher.hash(password);
        return userPersistenceAdapter.create(new User(id, username, passwordHash));
    }

}
