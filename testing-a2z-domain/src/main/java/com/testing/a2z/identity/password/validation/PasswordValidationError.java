package com.testing.a2z.identity.password.validation;

public sealed interface PasswordValidationError {

    record TooShort() implements PasswordValidationError {

    }

    record ContainsIllegalCharacter() implements PasswordValidationError {

    }

    record InsufficientOccurrences(PasswordCharacterType type) implements PasswordValidationError {

    }

}
