package com.testing.a2z.identity;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;

import org.junit.jupiter.api.Test;

// todo - test mora biti kao proza :D

class UserTest {

    @Test
    void shouldInstantiateUser() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPasswordHash = "givenPasswordHash";

        // when
        var actual = new User(givenId, givenUsername, givenPasswordHash);

        // then
        then(actual).isNotNull();
        then(actual).isInstanceOf(User.class);
        then(actual.id()).isEqualTo(givenId);
        then(actual.username()).isEqualTo(givenUsername);
        then(actual.passwordHash()).isEqualTo(givenPasswordHash);

        // todo - samo za showcase, pokazati rekurzivni compare?
    }

    @Test
    void shouldCompareIdenticalPasswordHashes() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPasswordHash = "givenPasswordHash";
        var givenUser = new User(givenId, givenUsername, givenPasswordHash);

        // when
        var actual = givenUser.passwordHashEquals(givenPasswordHash);

        // then
        then(actual).isTrue();
    }

    @Test
    void shouldCompareDifferentPasswordHashes() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPasswordHash = "givenPasswordHash";
        var givenUser = new User(givenId, givenUsername, givenPasswordHash);

        // when
        var actual = givenUser.passwordHashEquals("someOtherHash");

        // then
        then(actual).isFalse();
    }

}
