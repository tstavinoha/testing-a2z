package com.testing.a2z.identity.configuration;

import java.util.UUID;

import com.testing.a2z.identity.UserFactory;
import com.testing.a2z.identity.UserIdGenerator;
import com.testing.a2z.identity.UserService;
import com.testing.a2z.identity.adapter.out.persistence.UserPersistenceAdapter;
import com.testing.a2z.identity.password.PasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public PasswordHasher passwordHasher() {
        return originalString -> Integer.toString(originalString.hashCode());
    }

    @Bean
    public UserIdGenerator userIdGenerator() {
        return UUID::randomUUID;
    }

    @Bean
    public UserFactory userFactory(UserIdGenerator userIdGenerator,
                                   PasswordHasher passwordHasher) {
        return new UserFactory(userIdGenerator, passwordHasher);
    }

    @Bean
    public UserService userService(UserFactory userFactory,
                                   PasswordHasher passwordHasher,
                                   UserPersistenceAdapter userPersistenceAdapter) {
        return new UserService(userFactory,
                               passwordHasher,
                               userPersistenceAdapter,
                               userPersistenceAdapter);
    }

}
