package com.testing.a2z.identity.adapter.out.persistence;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;

import com.testing.a2z.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

class UserJpaRepositoryTest extends IntegrationTestBase {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void shouldPersistUser() {
        // given
        var givenUserId = UUID.randomUUID();
        var givenUsername = "givenUsername";
        var givenPasswordSalt = "givenPasswordSalt";
        var givenPasswordHash = "givenPasswordHash";

        var givenUser = new UserEntity(givenUserId, givenUsername, givenPasswordSalt, givenPasswordHash);

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

        var givenExistingUser = new UserEntity(UUID.randomUUID(), givenUsername, "salt", "passwordHash");
        var givenNewOtherUser = new UserEntity(UUID.randomUUID(), givenUsername, "salt", "passwordHash");

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
        var givenPasswordSalt = "givenPasswordSalt";
        var givenPasswordHash = "givenPasswordHash";

        var givenUser = new UserEntity(givenUserId, givenUsername, givenPasswordSalt, givenPasswordHash);
        userJpaRepository.save(givenUser);

        // when
        var result = userJpaRepository.findByUsername(givenUsername);

        // then
        then(result).contains(givenUser);
    }

}
