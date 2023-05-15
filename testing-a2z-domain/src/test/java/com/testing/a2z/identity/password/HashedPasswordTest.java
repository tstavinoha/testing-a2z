package com.testing.a2z.identity.password;

import static org.assertj.core.api.BDDAssertions.then;

import com.testing.a2z.identity.password.hashed.HashedPassword;
import com.testing.a2z.identity.password.hashed.Hasher;
import org.junit.jupiter.api.Test;

// todo - redoslijed - 2.
class HashedPasswordTest {

    @Test
    void shouldCreateHashedPassword() {
        // given
        var plainPassword = "plainPassword";
        Hasher hasher = input -> input;

        // when
        var result = HashedPassword.create(plainPassword, hasher);

        // then
        // NOTE: statics in tests are meh
        then(result.getSalt()).matches("[0-9a-f]{4}");
        then(result.getHash()).matches(plainPassword + "[0-9a-f]{4}");
    }

    @Test
    void shouldVerifyPassword() {
        // given
        var plainPassword = "plainPassword";
        var givenHashedPassword = HashedPassword.create(plainPassword, input -> input);

        // when
        var result = givenHashedPassword.verify(plainPassword);

        // then
        then(result).isTrue();
    }

    @Test
    void shouldNotVerifyWrongPassword() {
        // given
        var givenHashedPassword = HashedPassword.create("plainPassword", input -> input);

        // when
        var result = givenHashedPassword.verify("otherPassword");

        // then
        then(result).isFalse();
    }

}
