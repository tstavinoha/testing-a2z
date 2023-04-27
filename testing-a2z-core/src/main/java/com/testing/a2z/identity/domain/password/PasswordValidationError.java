package com.testing.a2z.identity.domain.password;

sealed interface PasswordValidationError {

    record TooShort() implements PasswordValidationError {

    }

    record ContainsIllegalCharacter() implements PasswordValidationError {

    }

    record InsufficientOccurrences(PasswordCharacterType type) implements PasswordValidationError {

    }

}
