package com.testing.a2z;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import java.time.Duration;
import java.util.UUID;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class StressTest extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");

    String username = "user" + UUID.randomUUID();
    String password = "p@ssWord0!234";

    ScenarioBuilder registerUser = scenario("Register User")
        .exec(http("request").post("/register")
                             .header("Content-Type", "application/json")
                             .body(StringBody("""
                                                  {
                                                  "username": "%s",
                                                  "password": "%s"
                                                  }
                                                  """.formatted(username, password)))
             );

    ScenarioBuilder verifyPassword = scenario("Verify Password")
        .exec(http("request").get("/verify")
                             .queryParam("username", username)
                             .queryParam("password", password)
             );

    {
        setUp(
            registerUser.injectOpen(atOnceUsers(1)).andThen(
                  verifyPassword.injectOpen(constantUsersPerSec(750).during(Duration.ofSeconds(30))))
             )
            .assertions(global().failedRequests().count().is(0L))
            .protocols(httpProtocol);
    }

}
