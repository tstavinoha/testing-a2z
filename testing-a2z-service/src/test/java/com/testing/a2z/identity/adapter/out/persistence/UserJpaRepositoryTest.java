package com.testing.a2z.identity.adapter.out.persistence;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;

import com.testing.a2z.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

// redoslijed - 5 - pokazati i IntegrationTestBase, i da bi ovi testovi pucali bez ciscenja baze (izolacija)
class UserJpaRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void shouldPersistUser() {
        // given
        var givenUserId = UUID.randomUUID();
        var givenUsername = "givenUsername";
        var givenPasswordHash = "givenPasswordHash";

        var givenUser = new UserEntity(givenUserId, givenUsername, givenPasswordHash);

        // when
        userJpaRepository.save(givenUser);

        // then
        var result = userJpaRepository.findById(givenUserId);
        then(result).contains(givenUser);
    }

    @Test
    void shouldFailToPersistUserWithDuplicateUsername() {
        // given
        var givenUsername = "givenUsername";

        var givenExistingUser = new UserEntity(UUID.randomUUID(), givenUsername, "passwordHash");
        var givenNewOtherUser = new UserEntity(UUID.randomUUID(), givenUsername, "passwordHash");

        // when
        userJpaRepository.save(givenExistingUser);
        var result = catchException(() -> userJpaRepository.save(givenNewOtherUser));

        // then
        then(result).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFindUserByUsername() {
        // given
        var givenUserId = UUID.randomUUID();
        var givenUsername = "givenUsername";
        var givenPasswordHash = "givenPasswordHash";

        var givenUser = new UserEntity(givenUserId, givenUsername, givenPasswordHash);
        userJpaRepository.save(givenUser);

        // when
        var result = userJpaRepository.findByUsername(givenUsername);

        // then
        then(result).contains(givenUser);
    }

}
