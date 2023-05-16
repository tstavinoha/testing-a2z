package com.testing.a2z.identity.password.validation;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PasswordCharacterTypeTest {

    @ParameterizedTest
    @MethodSource("passwordCharacterMappingDataSet")
    void shouldResolveCorrectly(Character givenCharacter, PasswordCharacterType expectedType) {
        // when
        var actual = PasswordCharacterType.from(givenCharacter);

        //then
        then(actual).isEqualTo(expectedType);
    }

    private static Stream<Arguments> passwordCharacterMappingDataSet() {
        return Stream.of(Arguments.of('a', PasswordCharacterType.LOWERCASE_LETTER),
                         Arguments.of('Ź', PasswordCharacterType.UPPERCASE_LETTER),
                         Arguments.of('9', PasswordCharacterType.DIGIT),
                         Arguments.of('!', PasswordCharacterType.SPECIAL),
                         Arguments.of(' ', PasswordCharacterType.ILLEGAL),
                         Arguments.of(null, PasswordCharacterType.ILLEGAL),
                         Arguments.of("🧛🏻".charAt(0), PasswordCharacterType.ILLEGAL));
    }

}
