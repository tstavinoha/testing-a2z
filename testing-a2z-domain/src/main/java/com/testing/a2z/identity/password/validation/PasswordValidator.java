package com.testing.a2z.identity.password.validation;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Map;
import java.util.Objects;

// todo - redoslijed: 2
public class PasswordValidator {

    private static final Integer MINIMUM_LENGTH = 10;
    private static final Map<PasswordCharacterType, Integer> MINIMUM_OCCURRENCES = Map.of(PasswordCharacterType.LOWERCASE_LETTER, 1,
                                                                                          PasswordCharacterType.UPPERCASE_LETTER, 1,
                                                                                          PasswordCharacterType.DIGIT, 1,
                                                                                          PasswordCharacterType.SPECIAL, 1);

    public static void validate(String password) {

        if (Objects.isNull(password) || password.length() < MINIMUM_LENGTH) {
            failValidation(new PasswordValidationError.TooShort());
        }

        var characterTypeCounts = password.chars()
                                          .mapToObj(character -> (char) character)
                                          .collect(groupingBy(PasswordCharacterType::from, counting()));

        if (characterTypeCounts.containsKey(PasswordCharacterType.ILLEGAL)) {
            failValidation(new PasswordValidationError.ContainsIllegalCharacter());
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
                           .ifPresent(insufficientPasswordCharacterType -> failValidation(new PasswordValidationError.InsufficientOccurrences(insufficientPasswordCharacterType)));
    }

    private static void failValidation(PasswordValidationError passwordValidationError) {
        throw new InvalidPasswordException(passwordValidationError);
    }

}
