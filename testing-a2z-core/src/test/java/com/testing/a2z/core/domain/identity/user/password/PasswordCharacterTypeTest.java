package com.testing.a2z.core.domain.identity.user.password;

import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.DIGIT;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.ILLEGAL;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.LOWERCASE_LETTER;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.SPECIAL;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.UPPERCASE_LETTER;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
        return Stream.of(Arguments.of('a', LOWERCASE_LETTER),
                         Arguments.of('Ź', UPPERCASE_LETTER),
                         Arguments.of('9', DIGIT),
                         Arguments.of('!', SPECIAL),
                         Arguments.of(' ', ILLEGAL),
                         Arguments.of(null, ILLEGAL),
                         Arguments.of("🧛🏻".charAt(0), ILLEGAL));
    }

}
