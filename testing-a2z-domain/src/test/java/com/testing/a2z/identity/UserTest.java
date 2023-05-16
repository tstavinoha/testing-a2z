package com.testing.a2z.identity;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.UUID;

import com.testing.a2z.identity.password.Password;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

class UserTest {

    @Test
    void shouldInstantiateUser() {
        // given
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var username = "username";
        var password = Mockito.mock(Password.class);

        // when
        var result = new User(id, username, password);

        // then
        then(result).isNotNull();
        then(result).isInstanceOf(User.class);
        then(result.id()).isEqualTo(id);
        then(result.username()).isEqualTo(username);
        then(result.password()).isEqualTo(password);
    }

    @Test
    void shouldVerifyPassword() {
        // given
        var id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var username = "username";
        var plainPassword = "password";
        var password = Mockito.mock(Password.class);

        // NOTE: In the first test the created user was the subject of the test, here it is a part of preparations
        var user = new User(id, username, password);

        given(password.verify(anyString())).willReturn(true);

        // when
        var result = user.verifyPassword(plainPassword);

        // then
        then(result).isTrue();

        BDDMockito.then(password).should().verify(plainPassword);
    }

}
