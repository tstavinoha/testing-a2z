package com.testing.a2z.identity.application.port.in;

public record CreateUserCommand(/*@NotNull*/ String username,
                                String password) {

}
