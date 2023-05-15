package com.testing.a2z.identity.adapter;

import com.testing.a2z.identity.User;
import com.testing.a2z.identity.UserFactory;
import com.testing.a2z.identity.adapter.out.persistence.UserPersistenceAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserHelper {

    private final UserPersistenceAdapter userPersistenceAdapter;
    private final UserFactory userFactory;

    public User givenUser(String username, String password) {
        var user = userFactory.createUser(username, password);
        return userPersistenceAdapter.create(user);
    }

    public String givenValidPassword() {
        return "Val1d_Password";
    }

}
