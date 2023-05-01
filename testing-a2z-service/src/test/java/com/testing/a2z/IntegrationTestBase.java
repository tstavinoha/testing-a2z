package com.testing.a2z;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.repository.CrudRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTestBase {

    @Autowired
    private List<CrudRepository<?, ?>> allRepositories;

    @LocalServerPort
    private Integer localServerPort;

    @BeforeEach
    public void setUp() {
        RestAssured.port = localServerPort;
    }

    @AfterEach
    public void cleanUp() {
        allRepositories.forEach(CrudRepository::deleteAll);
    }

}
