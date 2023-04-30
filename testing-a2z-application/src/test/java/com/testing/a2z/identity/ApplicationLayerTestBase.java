package com.testing.a2z.identity;

import com.testing.a2z.identity.password.PasswordHasher;
import com.testing.a2z.identity.port.out.CreateUserPort;
import com.testing.a2z.identity.port.out.FindUserPort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApplicationLayerTestBase {

    @InjectMocks
    protected UserService userService;

    @Mock
    protected UserFactory userFactory;

    @Mock
    protected PasswordHasher passwordHasher;

    @Mock
    protected FindUserPort findUserPort;

    @Mock
    protected CreateUserPort createUserPort;

}
