package com.testing.a2z.identity.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface UserJpaRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

}
