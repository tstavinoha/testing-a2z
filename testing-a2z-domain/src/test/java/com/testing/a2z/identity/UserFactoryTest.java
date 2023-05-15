package com.testing.a2z.identity;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.UUID;

import com.testing.a2z.identity.password.hashed.HashedPassword;
import com.testing.a2z.identity.password.hashed.HashedPasswordFactory;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

//  todo - redoslijed 4 - primjer mockanja 1, bez anotacija

class UserFactoryTest {

    UserIdGenerator userIdGenerator = Mockito.mock(UserIdGenerator.class);
    HashedPasswordFactory hashedPasswordFactory = Mockito.mock(HashedPasswordFactory.class);
    UserFactory userFactory = new UserFactory(userIdGenerator, hashedPasswordFactory);

    @Test
    void shouldCreateUser() {
        // given
        var givenId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";
        var givenHashedPassword = Mockito.mock(HashedPassword.class);

        BDDMockito.given(userIdGenerator.generate()).willReturn(givenId);
        BDDMockito.given(hashedPasswordFactory.create(anyString())).willReturn(givenHashedPassword);

        // when
        var actual = userFactory.createUser(givenUsername, givenPassword);

        // then
        var expectedUser = new User(givenId, givenUsername, givenHashedPassword);
        then(actual).isEqualTo(expectedUser);

        BDDMockito.then(userIdGenerator).should().generate();
        BDDMockito.then(hashedPasswordFactory).should().create(givenPassword);
    }

}
