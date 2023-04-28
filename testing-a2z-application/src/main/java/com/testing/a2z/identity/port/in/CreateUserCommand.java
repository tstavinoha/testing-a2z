package com.testing.a2z.identity.port.in;

public record CreateUserCommand(/*@NotNull*/ String username,
                                String password) {

}
