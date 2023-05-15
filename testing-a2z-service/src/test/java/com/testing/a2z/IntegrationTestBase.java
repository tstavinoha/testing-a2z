package com.testing.a2z;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.List;
import java.util.UUID;

import com.testing.a2z.identity.UserIdGenerator;
import com.testing.a2z.identity.adapter.UserHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.repository.CrudRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTestBase {

    @Autowired
    private List<CrudRepository<?, ?>> allRepositories;

    @Autowired
    protected UserHelper userHelper;

    @SpyBean
    protected UserIdGenerator userIdGenerator;

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

    protected UUID givenNextGeneratedId() {
        var uuid = UUID.randomUUID();
        BDDMockito.given(userIdGenerator.generate())
                  .willReturn(uuid)
                  .willCallRealMethod();
        return uuid;
    }

}
