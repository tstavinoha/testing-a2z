package com.testing.a2z.identity.adapter.in.web;

import static org.assertj.core.api.BDDAssertions.then;

import com.testing.a2z.IntegrationTestBase;
import com.testing.a2z.identity.adapter.UserHelper;
import io.restassured.RestAssured;
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
        var givenPassword = "givenPassword";

        userHelper.givenUser(givenUsername, givenPassword);

        // when
        var result = RestAssured.with()
                                .queryParam("username", givenUsername)
                                .queryParam("password", givenPassword)
                                .get("/verify")
                                .andReturn();

        // then
        then(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(result.body().asString()).isEqualTo(Boolean.TRUE.toString());
    }

    @Test
    void shouldNotVerifyUser() {
        // given
        var givenUsername = "givenUsername";
        var givenPassword = "givenPassword";

        userHelper.givenUser(givenUsername, "givenOtherPassword");

        // when
        var result = RestAssured.with()
                                .queryParam("username", givenUsername)
                                .queryParam("password", givenPassword)
                                .get("/verify")
                                .andReturn();

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
        var result = RestAssured.with()
                                .queryParam("username", givenUsername)
                                .queryParam("password", givenPassword)
                                .get("/verify")
                                .andReturn();

        // then
        then(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        then(result.body().asString()).isEqualTo(Boolean.FALSE.toString());
    }

}
