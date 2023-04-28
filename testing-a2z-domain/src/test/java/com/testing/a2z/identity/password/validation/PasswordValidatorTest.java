package com.testing.a2z.identity.password.validation;

import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import com.testing.a2z.identity.password.PasswordCharacterType;
import com.testing.a2z.identity.password.validation.PasswordValidationError.ContainsIllegalCharacter;
import com.testing.a2z.identity.password.validation.PasswordValidationError.InsufficientOccurrences;
import com.testing.a2z.identity.password.validation.PasswordValidationError.TooShort;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @Test
    void shouldPassValidationForValidPassword() {
        // given
        String givenPassword = "plsWORK_0k?";

        // when
        var actual = catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isNull();
    }

    @Test
    void shouldFailValidationForNull() {
        // given
        String givenPassword = null;

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isEqualTo(new InvalidPasswordException(new TooShort()));
    }

    @Test
    void shouldFailValidationForShortPassword() {
        // given
        String givenPassword = "sH0R+";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isEqualTo(new InvalidPasswordException(new TooShort()));
    }

    @Test
    void shouldFailValidationForPasswordContainingIllegalCharacters() {
        // given
        String givenPassword = "i CONTAIN 3 spaces!";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isEqualTo(new InvalidPasswordException(new ContainsIllegalCharacter()));
    }

    @Test
    void shouldFailValidationForPasswordWithoutLowercaseString() {
        // given
        String givenPassword = "NO_LOWERCASE_123";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isEqualTo(new InvalidPasswordException(new InsufficientOccurrences(PasswordCharacterType.LOWERCASE_LETTER)));
    }

    @Test
    void shouldFailValidationForPasswordWithoutUppercaseString() {
        // given
        String givenPassword = "no_uppercase_123";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(PasswordCharacterType.UPPERCASE_LETTER));
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordWithoutDigits() {
        // given
        String givenPassword = "No_Digits_!!!";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        then(actual).isEqualTo(new InvalidPasswordException(new InsufficientOccurrences(PasswordCharacterType.DIGIT)));
    }

    @Test
    void shouldFailValidationForPasswordWithoutSpecialCharacters() {
        // given
        String givenPassword = "NoSpecialCharacters1";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(PasswordCharacterType.SPECIAL));
        then(actual).isEqualTo(expectedException);
    }

}
