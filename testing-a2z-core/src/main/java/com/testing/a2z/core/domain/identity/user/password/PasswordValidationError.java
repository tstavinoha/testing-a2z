package com.testing.a2z.core.domain.identity.user.password;

sealed interface PasswordValidationError {

    record TooShort() implements PasswordValidationError {

    }

    record ContainsIllegalCharacter() implements PasswordValidationError {

    }

    record InsufficientOccurrences(PasswordCharacterType type) implements PasswordValidationError {

    }

}
