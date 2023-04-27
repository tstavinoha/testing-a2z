package com.testing.a2z.identity.application.port.in;

public interface VerifyPasswordQuery {

    boolean verify(String username, String password);

}
