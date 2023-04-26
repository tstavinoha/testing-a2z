package com.testing.a2z.core.domain.identity.user.password;

import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.DIGIT;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.ILLEGAL;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.LOWERCASE_LETTER;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.SPECIAL;
import static com.testing.a2z.core.domain.identity.user.password.PasswordCharacterType.UPPERCASE_LETTER;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Map;
import java.util.Objects;

import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.ContainsIllegalCharacter;
import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.InsufficientOccurrences;
import com.testing.a2z.core.domain.identity.user.password.PasswordValidationError.TooShort;

public class PasswordValidator {

    private static final Integer MINIMUM_LENGTH = 10;
    private static final Map<PasswordCharacterType, Integer> MINIMUM_OCCURRENCES = Map.of(LOWERCASE_LETTER, 1,
                                                                                          UPPERCASE_LETTER, 1,
                                                                                          DIGIT, 1,
                                                                                          SPECIAL, 1);

    public static void validate(String password) {

        if (Objects.isNull(password) || password.length() < MINIMUM_LENGTH) {
            failValidation(new TooShort());
        }

        var characterTypeCounts = password.chars()
                                          .mapToObj(character -> (char) character)
                                          .collect(groupingBy(PasswordCharacterType::from, counting()));

        if (characterTypeCounts.containsKey(ILLEGAL)) {
            failValidation(new ContainsIllegalCharacter());
        }

        validateMinimumOccurrences(characterTypeCounts);
    }

    private static void validateMinimumOccurrences(Map<PasswordCharacterType, Long> characterTypeCounts) {
        MINIMUM_OCCURRENCES.keySet()
                           .stream()
                           .filter(requiredType -> {
                              var minOccurrence = MINIMUM_OCCURRENCES.get(requiredType);
                              var actualOccurrence = characterTypeCounts.getOrDefault(requiredType, 0L).intValue();
                              return actualOccurrence < minOccurrence;
                          })
                           .findAny()
                           .ifPresent(insufficientPasswordCharacterType -> failValidation(new InsufficientOccurrences(insufficientPasswordCharacterType)));
    }

    private static void failValidation(PasswordValidationError passwordValidationError) {
        throw new InvalidPasswordException(passwordValidationError);
    }

}
