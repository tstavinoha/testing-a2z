package com.testing.a2z.identity.adapter.out.persistence;

import java.util.Optional;

import com.testing.a2z.identity.User;
import com.testing.a2z.identity.password.hashed.HashedPassword;
import com.testing.a2z.identity.password.hashed.HashedPasswordFactory;
import com.testing.a2z.identity.port.out.CreateUserPort;
import com.testing.a2z.identity.port.out.FindUserPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
// todo - test - unit ili integration?
public class UserPersistenceAdapter implements FindUserPort, CreateUserPort {

    private final UserJpaRepository userJpaRepository;
    private final HashedPasswordFactory hashedPasswordFactory;

    @Override
    public User create(User user) {
        var userEntity = mapToJpaEntity(user);
        var savedEntity = userJpaRepository.save(userEntity);
        return mapToDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> find(String username) {
        return userJpaRepository.findByUsername(username)
                                .map(this::mapToDomainEntity);
    }

    private UserEntity mapToJpaEntity(User user) {
        var hashedPassword = (HashedPassword) user.password(); // NOTE: Proper mapper skipped for simplicity
        return new UserEntity(user.id(), user.username(), hashedPassword.getSalt(), hashedPassword.getHash());
    }

    private User mapToDomainEntity(UserEntity userEntity) {
        return new User(userEntity.getId(),
                        userEntity.getUsername(),
                        hashedPasswordFactory.create(userEntity.getPasswordSalt(),
                                                     userEntity.getPasswordHash()));
    }

}
