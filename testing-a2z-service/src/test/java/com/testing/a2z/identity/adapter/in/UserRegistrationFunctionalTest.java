package com.testing.a2z.identity.adapter.in;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;

import com.testing.a2z.IntegrationTestBase;
import com.testing.a2z.identity.adapter.in.web.RegistrationResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

// todo redoslijed - 7 - pokazuje spy bean kako overrideati UserId Generator
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

    // todo - when user already registered
    // todo - when invalid password
    // todo - web test with mocks

    private Response whenUserRegistrationRequested(String givenUsername, String givenPassword) {
        return RestAssured.with()
                          .body(Map.of("username", givenUsername,
                                       "password", givenPassword))
                          .contentType(ContentType.JSON)
                          .post("/register")
                          .andReturn();
    }

}
