package com.testing.a2z.identity.domain;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldInstantiateUser() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword_1";

        // when
        var actual = new User(givenId, givenUsername, givenPassword);

        // then
        then(actual).isNotNull();
        then(actual).isInstanceOf(User.class);
        then(actual.id()).isEqualTo(givenId);
        then(actual.username()).isEqualTo(givenUsername);
        then(actual.password()).isEqualTo(givenPassword);
    }

    @Test
    void shouldNotInstantiateUserWithoutId() {
        // given
        UUID givenNullId = null;
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword_1";

        // when
        var actual = catchException(() -> new User(givenNullId, givenUsername, givenPassword));

        // then
        then(actual).isInstanceOf(NullPointerException.class);
    }

}
