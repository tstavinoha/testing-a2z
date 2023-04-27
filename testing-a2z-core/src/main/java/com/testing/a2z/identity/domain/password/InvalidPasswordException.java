package com.testing.a2z.identity.domain.password;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class InvalidPasswordException extends RuntimeException {

    PasswordValidationError passwordValidationError;

    public InvalidPasswordException(PasswordValidationError passwordValidationError) {
        super("Password validation failed because of error: " + passwordValidationError);
        this.passwordValidationError = passwordValidationError;
    }


}
