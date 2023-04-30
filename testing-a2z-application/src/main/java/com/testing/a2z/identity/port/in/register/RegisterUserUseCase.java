package com.testing.a2z.identity.port.in.register;

import com.testing.a2z.identity.User;

public interface RegisterUserUseCase {

    User register(RegisterUserCommand registerUserCommand);

}
