package com.testing.a2z.identity.password.hashed;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

class HashedPasswordFactoryTest {

    SaltGenerator saltGenerator = Mockito.mock(SaltGenerator.class);
    Hasher hasher = Mockito.mock(Hasher.class);
    HashedPasswordFactory hashedPasswordFactory = new HashedPasswordFactory(saltGenerator, hasher);

    @Test
    void shouldCreateHashedPassword() {
        // given
        var givenSalt = "givenSalt";
        var givenPlainPassword = "1VeryCoolPassword!"; // NOTE: Must be a valid password due to static nature of PasswordValidator :(
        var givenHashedPassword = "givenHashedPassword";

        BDDMockito.given(saltGenerator.generate()).willReturn(givenSalt);
        BDDMockito.given(hasher.hash(anyString())).willReturn(givenHashedPassword);

        // when
        var actual = hashedPasswordFactory.create(givenPlainPassword);

        // then
        var expected = new HashedPassword(givenSalt, givenHashedPassword, hasher);
        then(actual).isEqualTo(expected);

        BDDMockito.then(saltGenerator).should().generate();
        BDDMockito.then(hasher).should().hash(givenPlainPassword + givenSalt);
    }

}