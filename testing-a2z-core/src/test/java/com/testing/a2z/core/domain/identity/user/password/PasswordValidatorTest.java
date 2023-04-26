package com.testing.a2z.core.domain.identity.user.password;

import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.DIGIT;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.LOWERCASE_LETTER;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.SPECIAL;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.UPPERCASE_LETTER;
import static org.assertj.core.api.BDDAssertions.catchException;
import static org.assertj.core.api.BDDAssertions.then;

import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.ContainsIllegalCharacter;
import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.InsufficientOccurrences;
import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.TooShort;
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
        var expectedException = new InvalidPasswordException(new TooShort());
        then(actual).usingRecursiveComparison().isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForShortPassword() {
        // given
        String givenPassword = "sH0R+";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new TooShort());
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordContainingIllegalCharacters() {
        // given
        String givenPassword = "i CONTAIN 3 spaces!";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new ContainsIllegalCharacter());
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordWithoutLowercaseString() {
        // given
        String givenPassword = "NO_LOWERCASE_123";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(LOWERCASE_LETTER));
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordWithoutUppercaseString() {
        // given
        String givenPassword = "no_uppercase_123";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(UPPERCASE_LETTER));
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordWithoutDigits() {
        // given
        String givenPassword = "No_Digits_!!!";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(DIGIT));
        then(actual).isEqualTo(expectedException);
    }

    @Test
    void shouldFailValidationForPasswordWithoutSpecialCharacters() {
        // given
        String givenPassword = "NoSpecialCharacters1";

        // when
        var actual = BDDAssertions.catchException(() -> PasswordValidator.validate(givenPassword));

        // then
        var expectedException = new InvalidPasswordException(new InsufficientOccurrences(SPECIAL));
        then(actual).isEqualTo(expectedException);
    }

}
