package com.testing.a2z.infrastructure;

import java.util.UUID;

import com.testing.a2z.identity.UserIdGenerator;
import org.springframework.stereotype.Component;

@Component
public class RandomUuidGenerator implements UserIdGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }

}
