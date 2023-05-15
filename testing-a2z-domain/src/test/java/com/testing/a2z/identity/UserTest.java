package com.testing.a2z.identity;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import com.testing.a2z.identity.password.HashedPassword;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

// TODO - test mora biti kao proza :D
// TODO - redoslijed 1. Pokazati neki super jednostavni test tijekom prezentacije da nije preveliki sok.
// ovdje su showcaseani
// 1. - jednostavni inline mockito, assertanje resultata malo po malo
// 2. - mockanje poziva i provjera da je mock ispravno pozvan
// 3. - showcase za izazivanje i provjeru exceptiona

class UserTest {

    @Test
    void shouldInstantiateUser() {
        // given
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var username = "username";
        var hashedPassword = Mockito.mock(HashedPassword.class);

        // when
        var result = new User(id, username, hashedPassword);

        // then
        then(result).isNotNull();
        then(result).isInstanceOf(User.class);
        then(result.id()).isEqualTo(id);
        then(result.username()).isEqualTo(username);
        then(result.hashedPassword()).isEqualTo(hashedPassword);
    }

    @Test
    void shouldVerifyPassword() {
        // given
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var username = "username";
        var password = "password";
        var hashedPassword = Mockito.mock(HashedPassword.class);
        // NOTE: In the first test the created user was the subject of the test, here it is a part of preparations
        var user = new User(id, username, hashedPassword);

        given(hashedPassword.verify(anyString())).willReturn(true);

        // when
        var result = user.verifyPassword(password);

        // then
        then(result).isTrue();

        BDDMockito.then(hashedPassword).should().verify(password);
    }

    @Test
    void shouldShowcaseHandlingException() {
        // given
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var username = "username";
        var password = "password";
        var hashedPassword = Mockito.mock(HashedPassword.class);
        var user = new User(id, username, hashedPassword);

        given(hashedPassword.verify(anyString())).willThrow(new IllegalArgumentException());

        // when
        var result = catchException(() -> user.verifyPassword(password));

        // then
        then(result).isInstanceOf(IllegalArgumentException.class);
        then(result).isInstanceOf(RuntimeException.class);
    }

}
