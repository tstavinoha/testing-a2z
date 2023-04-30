package com.testing.a2z.identity.port.in.verify;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.UUID;

import com.testing.a2z.identity.ApplicationLayerTestBase;
import com.testing.a2z.identity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

public class VerifyPasswordQueryTest extends ApplicationLayerTestBase {

    private VerifyPasswordQuery verifyPasswordQuery;

    @BeforeEach
    void setUp() {
        verifyPasswordQuery = userService;
    }

    @Test
    void shouldVerifyPassword() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";
        var givenPasswordHash = "givenPasswordHash";

        givenUser(givenUsername, givenPasswordHash);
        givenPasswordHashing(givenPassword, givenPasswordHash);

        // when
        var result = verifyPasswordQuery.verify(givenUsername, givenPassword);

        // then
        then(result).isTrue();
        BDDMockito.then(findUserPort).should().find(givenUsername);
        BDDMockito.then(passwordHasher).should().hash(givenPassword);
    }

    @Test
    void shouldNotVerifyWrongPassword() {
        // given
        var givenUsername = "givenUsername";
        var givenUserPasswordHash = "givenUserPasswordHash";
        var givenWrongPassword = "givenWrongPassword";
        var givenWrongPasswordHash = "givenWrongPasswordHash";

        givenUser(givenUsername, givenUserPasswordHash);
        givenPasswordHashing(givenWrongPassword, givenWrongPasswordHash);

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

    private void givenUser(String username, String passwordHash) {
        var user = new User(UUID.randomUUID(), username, passwordHash);
        BDDMockito.given(findUserPort.find(any())).willReturn(Optional.of(user));
    }

    private void givenNoUserFound() {
        BDDMockito.given(findUserPort.find(any())).willReturn(Optional.empty());
    }

    private void givenPasswordHashing(String givenPassword, String givenPasswordHash) {
        BDDMockito.given(passwordHasher.hash(givenPassword)).willReturn(givenPasswordHash);
    }

}
