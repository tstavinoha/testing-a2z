package com.testing.a2z.identity.application.port.in;

import com.testing.a2z.identity.domain.User;

public interface CreateUserUseCase {

    User create(CreateUserCommand createUserCommand);

}
