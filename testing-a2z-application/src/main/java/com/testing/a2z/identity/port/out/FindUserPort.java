package com.testing.a2z.identity.port.out;

import java.util.Optional;

import com.testing.a2z.identity.User;

public interface FindUserPort {

    Optional<User> find(String username);

}
