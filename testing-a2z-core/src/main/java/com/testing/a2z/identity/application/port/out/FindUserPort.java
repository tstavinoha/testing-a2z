package com.testing.a2z.identity.application.port.out;

import java.util.Optional;

import com.testing.a2z.identity.domain.User;

public interface FindUserPort {

    Optional<User> find(String username);

}
