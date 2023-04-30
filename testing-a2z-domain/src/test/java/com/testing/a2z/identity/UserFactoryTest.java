package com.testing.a2z.identity;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;

import java.util.UUID;

import com.testing.a2z.identity.password.PasswordHasher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

// todo - redoslijed 4 - primjer mockanja 1, bez anotacija

class UserFactoryTest {

    UserIdGenerator userIdGenerator = Mockito.mock(UserIdGenerator.class);
    PasswordHasher passwordHasher = Mockito.mock(PasswordHasher.class);
    UserFactory userFactory = new UserFactory(userIdGenerator, passwordHasher);

    @Test
    void shouldCreateUser() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPassword = "1VeryCoolPassword!"; // todo - pitanje => zasto ovo mora biti validan password? staticke klase i testiranje
        var givenPasswordHash = "givenPasswordHash";

        BDDMockito.given(userIdGenerator.generate()).willReturn(givenId);
        BDDMockito.given(passwordHasher.hash(any())).willReturn(givenPasswordHash);

        // when
        var actual = userFactory.createUser(givenUsername, givenPassword);

        // then
        var expectedUser = new User(givenId, givenUsername, givenPasswordHash);
        then(actual).isEqualTo(expectedUser);

        BDDMockito.then(userIdGenerator).should().generate();
        BDDMockito.then(passwordHasher).should().hash(givenPassword);
    }

}
