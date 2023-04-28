package com.testing.a2z.identity.port.in;

import com.testing.a2z.identity.User;

public interface CreateUserUseCase {

    User create(CreateUserCommand createUserCommand);

}
