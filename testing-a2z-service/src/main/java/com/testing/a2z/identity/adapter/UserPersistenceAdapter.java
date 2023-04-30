package com.testing.a2z.identity.adapter;

import java.util.Optional;

import com.testing.a2z.identity.User;
import com.testing.a2z.identity.adapter.jpa.UserEntity;
import com.testing.a2z.identity.adapter.jpa.UserJpaRepository;
import com.testing.a2z.identity.port.out.CreateUserPort;
import com.testing.a2z.identity.port.out.FindUserPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
// todo - test - unit ili integration?
public class UserPersistenceAdapter implements FindUserPort, CreateUserPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User create(User user) {
        var userEntity = new UserEntity(user.id(), user.username(), user.passwordHash());
        var savedEntity = userJpaRepository.save(userEntity);
        return mapToDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> find(String username) {
        return userJpaRepository.findByUsername(username)
                                .map(this::mapToDomainEntity);
    }

    private User mapToDomainEntity(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPasswordHash());
    }

}
