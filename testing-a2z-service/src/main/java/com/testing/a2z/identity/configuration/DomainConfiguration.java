package com.testing.a2z.identity.configuration;

import java.util.UUID;

import com.testing.a2z.identity.UserFactory;
import com.testing.a2z.identity.UserIdGenerator;
import com.testing.a2z.identity.UserService;
import com.testing.a2z.identity.adapter.out.persistence.UserPersistenceAdapter;
import com.testing.a2z.identity.password.hashed.HashedPasswordFactory;
import com.testing.a2z.identity.password.hashed.Hasher;
import com.testing.a2z.identity.password.hashed.SaltGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public Hasher hasher() {
        return originalString -> Integer.toString(originalString.hashCode());
    }

    @Bean
    public HashedPasswordFactory hashedPasswordFactory(Hasher hasher) {
        SaltGenerator saltGenerator = () -> UUID.randomUUID().toString().substring(0, 4);
        return new HashedPasswordFactory(saltGenerator, hasher);
    }

    @Bean
    public UserFactory userFactory(UserIdGenerator userIdGenerator,
                                   HashedPasswordFactory hashedPasswordFactory) {
        return new UserFactory(userIdGenerator, hashedPasswordFactory);
    }

    @Bean
    public UserService userService(UserFactory userFactory,
                                   UserPersistenceAdapter userPersistenceAdapter) {
        return new UserService(userFactory,
                               userPersistenceAdapter,
                               userPersistenceAdapter);
    }

}
