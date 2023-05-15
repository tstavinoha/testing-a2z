package com.testing.a2z.identity.adapter.in;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;

import com.testing.a2z.IntegrationTestBase;
import com.testing.a2z.identity.adapter.UserHelper;
import com.testing.a2z.identity.adapter.in.web.RegistrationResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class UserRegistrationFunctionalTest extends IntegrationTestBase {

    @Test
    void shouldRegisterUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "validPassword_1!";
        var givenUserId = givenNextGeneratedId();

        // when
        var actual = whenUserRegistrationRequested(givenUsername, givenPassword);

        // then
        var expectedResponse = new RegistrationResponse(givenUserId.toString());
        then(actual.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(actual.body().as(RegistrationResponse.class)).isEqualTo(expectedResponse);
    }

    @Test
    void shouldFailToRegisterUserWithDuplicateUsername() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "validPassword_1!";

        userHelper.givenUser(givenUsername);

        // when
        var actual = whenUserRegistrationRequested(givenUsername, givenPassword);

        // then
        then(actual.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(actual.body().asString()).isEqualTo("Username already taken");
    }

    @Test
    void shouldFailToRegisterUserWithInvalidPassword() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "pass";

        // when
        var actual = whenUserRegistrationRequested(givenUsername, givenPassword);

        // then
        then(actual.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        then(actual.body().asString()).isEqualTo("Password is too short");
    }

    private Response whenUserRegistrationRequested(String givenUsername, String givenPassword) {
        return RestAssured.with()
                          .body(Map.of("username", givenUsername,
                                       "password", givenPassword))
                          .contentType(ContentType.JSON)
                          .post("/register")
                          .andReturn();
    }

}
