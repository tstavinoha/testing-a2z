package com.testing.a2z.identity.adapter.in.web;

import static com.testing.a2z.identity.password.PasswordCharacterType.UPPERCASE_LETTER;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.stream.Stream;

import com.testing.a2z.identity.User;
import com.testing.a2z.identity.password.HashedPassword;
import com.testing.a2z.identity.password.validation.InvalidPasswordException;
import com.testing.a2z.identity.password.validation.PasswordValidationError;
import com.testing.a2z.identity.password.validation.PasswordValidationError.ContainsIllegalCharacter;
import com.testing.a2z.identity.password.validation.PasswordValidationError.InsufficientOccurrences;
import com.testing.a2z.identity.password.validation.PasswordValidationError.TooShort;
import com.testing.a2z.identity.port.in.register.RegisterUserCommand;
import com.testing.a2z.identity.port.in.register.RegisterUserUseCase;
import com.testing.a2z.identity.port.in.register.UsernameTakenException;
import com.testing.a2z.identity.port.in.verify.VerifyPasswordQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

// todo redoslijed - 6 - "unit" test za web, nije bdd

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerifyPasswordQuery verifyPasswordQuery;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    public void shouldVerifyPassword() throws Exception {
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        givenVerificationResult(true);

        mockMvc.perform(get("/verify")
                            .queryParam("username", givenUsername)
                            .queryParam("password", givenPassword))
               .andExpectAll(status().is(HttpStatus.OK.value()),
                             content().string("true"));

        BDDMockito.then(verifyPasswordQuery).should().verify(givenUsername, givenPassword);
    }

    @Test
    public void shouldFailToVerifyPassword() throws Exception {
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        givenVerificationResult(false);

        mockMvc.perform(get("/verify")
                            .queryParam("username", givenUsername)
                            .queryParam("password", givenPassword))
               .andExpectAll(status().is(HttpStatus.OK.value()),
                             content().string("false"));

        BDDMockito.then(verifyPasswordQuery).should().verify(givenUsername, givenPassword);
    }

    @Test
    public void shouldDeclineVerificationWithoutUsername() throws Exception {
        mockMvc.perform(get("/verify")
                            .queryParam("password", "password"))
               .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        var givenUser = givenRegistrationSuccess(givenUsername);

        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(registrationBody(givenUsername, givenPassword)))
               .andExpectAll(status().is(HttpStatus.OK.value()),
                             content().string("""
                                                  {"id":"%s"}
                                                  """.formatted(givenUser.id()).trim()));

        BDDMockito.then(registerUserUseCase).should().register(new RegisterUserCommand(givenUsername, givenPassword));
    }

    @Test
    public void shouldFailToRegisterUserWithDuplicateUsername() throws Exception {
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        givenRegistrationFailure(new UsernameTakenException());

        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(registrationBody(givenUsername, givenPassword)))
               .andExpectAll(status().is(HttpStatus.BAD_REQUEST.value()),
                             content().string("Username already taken"));
    }

    @ParameterizedTest
    @MethodSource("invalidPasswordSource")
    public void shouldFailToRegisterUserWithInvalidPassword(PasswordValidationError passwordValidationError, String expectedResponseString) throws Exception {
        givenRejectedPassword(passwordValidationError);

        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(registrationBody("username", "password")))
               .andExpectAll(status().is(HttpStatus.BAD_REQUEST.value()),
                             content().string(expectedResponseString));
    }

    public static Stream<Arguments> invalidPasswordSource() {
        return Stream.of(Arguments.of(new TooShort(), "Password is too short"),
                         Arguments.of(new ContainsIllegalCharacter(), "Password contains an illegal character"),
                         Arguments.of(new InsufficientOccurrences(UPPERCASE_LETTER), "Password has insufficient occurences of " + UPPERCASE_LETTER));
    }

    @Test
    public void shouldRejectRegistrationWithoutUsername() throws Exception {
        mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(registrationBody("", "password")))
               .andExpectAll(status().is(HttpStatus.BAD_REQUEST.value()))
               .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));
    }

    private void givenVerificationResult(boolean verified) {
        given(verifyPasswordQuery.verify(any(), any())).willReturn(verified);
    }

    private User givenRegistrationSuccess(String givenUsername) {
        var givenUser = new User(UUID.randomUUID(), givenUsername, Mockito.mock(HashedPassword.class));
        BDDMockito.given(registerUserUseCase.register(any())).willReturn(givenUser);
        return givenUser;
    }

    private void givenRegistrationFailure(RuntimeException exception) {
        BDDMockito.given(registerUserUseCase.register(any())).willThrow(exception);
    }

    private void givenRejectedPassword(PasswordValidationError passwordValidationError) {
        givenRegistrationFailure(new InvalidPasswordException(passwordValidationError));
    }

    private static String registrationBody(String givenUsername, String givenPassword) {
        return """
            {
                "username": "%s",
                "password": "%s"
            }
             """.formatted(givenUsername, givenPassword);
    }

}
