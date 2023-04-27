package com.testing.a2z.identity.application;

import com.testing.a2z.identity.application.port.in.CreateUserCommand;
import com.testing.a2z.identity.application.port.in.CreateUserUseCase;
import com.testing.a2z.identity.application.port.in.VerifyPasswordQuery;
import com.testing.a2z.identity.domain.User;

public class UserService implements CreateUserUseCase, VerifyPasswordQuery {

    @Override
    public User create(CreateUserCommand createUserCommand) {

        return null;
    }

    @Override
    public boolean verify(String username, String password) {
        return false;
    }

}
