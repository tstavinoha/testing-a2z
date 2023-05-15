package com.testing.a2z.identity.port.in.verify;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import com.testing.a2z.identity.ApplicationLayerTestBase;
import com.testing.a2z.identity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;

public class VerifyPasswordQueryTest extends ApplicationLayerTestBase {

    private VerifyPasswordQuery verifyPasswordQuery;

    @Mock
    User user;

    @BeforeEach
    void setUp() {
        verifyPasswordQuery = userService;
    }

    @Test
    void shouldVerifyPassword() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        givenUser(givenUsername, givenPassword);

        // when
        var result = verifyPasswordQuery.verify(givenUsername, givenPassword);

        // then
        then(result).isTrue();
        thenUserHasBeenSearchedForByUsername(givenUsername);
        thenPasswordWasVerified(givenPassword);
    }

    @Test
    void shouldNotVerifyWrongPassword() {
        // given
        var givenUsername = "givenUsername";
        var givenUserPassword = "givenUserPassword";
        var givenWrongPassword = "givenWrongPassword";

        givenUser(givenUsername, givenUserPassword);

        // when
        var result = verifyPasswordQuery.verify(givenUsername, givenWrongPassword);

        // then
        then(result).isFalse();
    }

    @Test
    void shouldNotVerifyPasswordForUnknownUser() {
        // given
        var givenUsername = "givenUsername";

        givenNoUserFound();

        // when
        var result = verifyPasswordQuery.verify(givenUsername, "somePassword");

        // then
        then(result).isFalse();
    }

    private User givenUser(String username, String password) {
        given(findUserPort.find(username)).willReturn(Optional.of(user));
        given(user.verifyPassword(any())).willAnswer(args -> password.equals(args.getArgument(0).toString()));
        return user;
    }

    private void givenNoUserFound() {
        given(findUserPort.find(any())).willReturn(Optional.empty());
    }

    private void thenPasswordWasVerified(String givenPassword) {
        BDDMockito.then(user).should().verifyPassword(givenPassword);
    }

    private void thenUserHasBeenSearchedForByUsername(String givenUsername) {
        BDDMockito.then(findUserPort).should().find(givenUsername);
    }

}
