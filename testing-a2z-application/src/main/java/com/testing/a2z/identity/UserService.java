package com.testing.a2z.identity;

import com.testing.a2z.identity.port.in.CreateUserCommand;
import com.testing.a2z.identity.port.in.CreateUserUseCase;
import com.testing.a2z.identity.port.in.VerifyPasswordQuery;
import com.testing.a2z.identity.port.out.StoreUserPort;
import com.testing.a2z.identity.port.out.FindUserPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService implements CreateUserUseCase, VerifyPasswordQuery {

    private final FindUserPort findUserPort;
    private final StoreUserPort storeUserPort;

    @Override
    public User create(CreateUserCommand createUserCommand) {

        return null;
    }

    @Override
    public boolean verify(String username, String password) {
        return false;
    }

}
