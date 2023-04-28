package com.testing.a2z.identity.port.in;

public interface VerifyPasswordQuery {

    boolean verify(String username, String password);

}
