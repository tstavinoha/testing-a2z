package com.testing.a2z.identity.password.validation;

import com.testing.a2z.identity.password.PasswordCharacterType;

sealed interface PasswordValidationError {

    record TooShort() implements PasswordValidationError {

    }

    record ContainsIllegalCharacter() implements PasswordValidationError {

    }

    record InsufficientOccurrences(PasswordCharacterType type) implements PasswordValidationError {

    }

}
