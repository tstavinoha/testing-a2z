package com.testing.a2z;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;

@SpringBootTest
public class IntegrationTestBase {

    @Autowired
    List<CrudRepository<?, ?>> allRepositories;

    @AfterEach
    public void cleanUp() {
        allRepositories.forEach(CrudRepository::deleteAll);
    }

}
