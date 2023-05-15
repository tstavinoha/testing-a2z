package com.testing.a2z.identity.password;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

// todo - redoslijed 4, pokazati sve iz user factory testa
// primjer mockanja 1, bez anotacija
class HashedPasswordFactoryTest {

    SaltGenerator saltGenerator = Mockito.mock(SaltGenerator.class);
    Hasher hasher = Mockito.mock(Hasher.class);
    HashedPasswordFactory hashedPasswordFactory = new HashedPasswordFactory(saltGenerator, hasher);

    @Test
    void shouldCreateHashedPassword() {
        // given
        var givenSalt = "givenSalt";
        var givenPlainPassword = "1VeryCoolPassword!"; // todo - pitanje => zasto ovo mora biti validan password? staticke klase i testiranje
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
