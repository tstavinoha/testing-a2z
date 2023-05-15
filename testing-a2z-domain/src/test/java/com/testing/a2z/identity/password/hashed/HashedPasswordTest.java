package com.testing.a2z.identity.password.hashed;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class HashedPasswordTest {

    @Test
    void shouldCreateHashedPassword() {
        // given
        var plainPassword = "plainPassword";
        Hasher hasher = input -> input;

        // when
        var result = HashedPassword.create(plainPassword, hasher);

        // then
        // NOTE: non-pure statics in tests are meh:
        //      if they produce random output -> we can't fully test, and need to resort to asserting by regex
        //      if they trigger side effects -> can cause undesired or unpreventable behavior
        then(result.getSalt()).matches("[0-9a-f]{4}");
        then(result.getHash()).matches(plainPassword + "[0-9a-f]{4}");
    }

    @Test
    void shouldSaltenPassword() {
        // given
        var plainPassword = "plainPassword";
        var salt = "salt";

        // when
        var result = HashedPassword.salten(plainPassword, salt);

        // then
        var expected = plainPassword + salt;
        then(result).isEqualTo(expected);
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
