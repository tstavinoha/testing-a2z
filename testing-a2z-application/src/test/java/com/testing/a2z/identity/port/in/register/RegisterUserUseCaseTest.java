package com.testing.a2z.identity.port.in.register;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import com.testing.a2z.identity.ApplicationLayerTestBase;
import com.testing.a2z.identity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

class RegisterUserUseCaseTest extends ApplicationLayerTestBase {

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        this.registerUserUseCase = userService;
    }

    @Test
    void shouldRegisterUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";
        var givenRegisterUserCommand = new RegisterUserCommand(givenUsername, givenPassword);

        givenUsernameNotTaken();
        var givenUser = givenSuccessfulUserCreation();

        // when
        var actual = registerUserUseCase.register(givenRegisterUserCommand);

        // then
        then(actual).isEqualTo(givenUser);
        BDDMockito.then(findUserPort).should().find(givenRegisterUserCommand.username());
        BDDMockito.then(userFactory).should().createUser(givenUsername, givenPassword);
        BDDMockito.then(createUserPort).should().create(any(User.class));
    }

    @Test
    void shouldFailToRegisterUserIfUsernameTaken() {
        // given
        var registerUserCommand = new RegisterUserCommand("givenUsername", "givenPassword");

        givenUsernameTaken();

        // when
        var actual = catchException(() -> registerUserUseCase.register(registerUserCommand));

        // then
        then(actual).isInstanceOf(UsernameTakenException.class);
    }

    private void givenUsernameNotTaken() {
        BDDMockito.given(findUserPort.find(any())).willReturn(Optional.empty());
    }

    private void givenUsernameTaken() {
        BDDMockito.given(findUserPort.find(any())).willReturn(Optional.of(mock(User.class)));
    }

    private User givenSuccessfulUserCreation() {
        var givenCreatedUser = mock(User.class);
        BDDMockito.given(userFactory.createUser(any(), any())).willReturn(givenCreatedUser);
        BDDMockito.given(createUserPort.create(any())).willReturn(givenCreatedUser);
        return givenCreatedUser;
    }

}
