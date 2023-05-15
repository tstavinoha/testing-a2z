package com.testing.a2z.identity.adapter.in;

import static org.assertj.core.api.BDDAssertions.then;

import com.testing.a2z.IntegrationTestBase;
import com.testing.a2z.identity.adapter.UserHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class UserVerificationFunctionalTest extends IntegrationTestBase {

    @Autowired
    private UserHelper userHelper;

    @Test
    void shouldVerifyUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = userHelper.givenValidPassword();

        userHelper.givenUser(givenUsername, givenPassword);

        // when
        var result = whenVerificationAttempted(givenUsername, givenPassword);

        // then
        then(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(result.body().asString()).isEqualTo(Boolean.TRUE.toString());
    }

    @Test
    void shouldNotVerifyUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = userHelper.givenValidPassword();
        var givenWrongPassword = "wrongPassword";

        userHelper.givenUser(givenUsername, givenPassword);

        // when
        var result = whenVerificationAttempted(givenUsername, givenWrongPassword);

        // then
        then(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(result.body().asString()).isEqualTo(Boolean.FALSE.toString());
    }

    @Test
    void shouldSilentlyNotVerifyNonExistentUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        // when
        var result = whenVerificationAttempted(givenUsername, givenPassword);

        // then
        then(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(result.body().asString()).isEqualTo(Boolean.FALSE.toString());
    }

    private Response whenVerificationAttempted(String username, String password) {
        return RestAssured.with()
                          .queryParam("username", username)
                          .queryParam("password", password)
                          .get("/verify")
                          .andReturn();
    }

}
