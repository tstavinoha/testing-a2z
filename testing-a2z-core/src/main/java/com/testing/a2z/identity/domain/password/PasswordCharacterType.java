package com.testing.a2z.identity.domain.password;

import java.util.Objects;

enum PasswordCharacterType {
    UPPERCASE_LETTER,
    LOWERCASE_LETTER,
    DIGIT,
    SPECIAL,
    ILLEGAL;

    private static final String SPECIAL_CHARACTERS = "!?@#$%^&*()_+=-,./<>?§±`~{}:;\"'\\[]";

     static PasswordCharacterType from(Character character) {
         if (Objects.isNull(character)) {
             return ILLEGAL;
         }

        if (Character.isUpperCase(character)) {
            return PasswordCharacterType.UPPERCASE_LETTER;
        }

        if (Character.isLowerCase(character)) {
            return PasswordCharacterType.LOWERCASE_LETTER;
        }

        if (Character.isDigit(character)) {
            return PasswordCharacterType.DIGIT;
        }

        if (SPECIAL_CHARACTERS.indexOf(character) != -1) {
            return PasswordCharacterType.SPECIAL;
        }

        return ILLEGAL;
    }
}
