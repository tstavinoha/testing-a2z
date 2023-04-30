package com.testing.a2z.identity.port.in.register;

public record RegisterUserCommand(/*@NotNull*/ String username,
                                               String password) {

}
