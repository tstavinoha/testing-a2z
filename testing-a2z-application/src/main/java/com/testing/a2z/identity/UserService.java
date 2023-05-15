package com.testing.a2z.identity;

import com.testing.a2z.identity.port.in.register.RegisterUserCommand;
import com.testing.a2z.identity.port.in.register.RegisterUserUseCase;
import com.testing.a2z.identity.port.in.register.UsernameTakenException;
import com.testing.a2z.identity.port.in.verify.VerifyPasswordQuery;
import com.testing.a2z.identity.port.out.CreateUserPort;
import com.testing.a2z.identity.port.out.FindUserPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserService implements RegisterUserUseCase, VerifyPasswordQuery { //todo - test. koliko klasa ima smisla?

    private final UserFactory userFactory;
    private final FindUserPort findUserPort;
    private final CreateUserPort createUserPort;

    @Override
    public User register(RegisterUserCommand command) {
        if (findUserPort.find(command.username()).isPresent()) {
            throw new UsernameTakenException();
        }

        var newUser = userFactory.createUser(command.username(), command.password());
        return createUserPort.create(newUser);
    }

    @Override
    public boolean verify(String username, String password) {
        return findUserPort.find(username)
                           .map(user -> verify(user, password))
                           .orElse(false);
    }

    private boolean verify(User user, String password) {
        return user.verifyPassword(password);
    }

}
