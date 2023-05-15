# Testing - A to Ź

`Testing - A to Ź` is a small showcase for different types of tests, written in Java, accompanying the presentation intended for college students.

Please note:

> This project is a showcase and as such it has some design decision and flaws introduced to better emphasize the main subject of the presentation, which is testing.
> Some design decisions are purposely simplified and the architecture uses some shortcuts which would not be acceptable in a real-world project.
> Basically, the project is set up in a way that allows for progressive introduction of testing concepts.

## About

The project models a small backend service called `Identity Service` that is able to register users and verify their passwords.
It is not meant to be exposed to the outside world directly, but rather to be used by the public-facing services of a hypothetical platform. 

![](docs/00%20-%20C1%20-%20Architecture.svg)

The general architecture of the project follows the design philosophy of Clean Architecture (or at least, tries to).

![](docs/00%20-%20C3%20-%20Architecture.svg)

Testing technologies used in the project are:
- JUnit 5 (Jupiter) - base testing framework that provides the support for running tests
- Mockito - testing library for creation of shallow implementations and making up custom behavior of classes
- AssertJ - Fluent assertion library, that makes our test more readable and concise
- RestAssured - Helps with sending HTTP requests in functional tests
- Gatling - Stress test tool

TODO - Spring, H2, 

## Running the project tests

### Pre-requisites
- Java 20
- Maven
- Intellij IDEA (optional, use for best results)

### Running JUnit tests (unit, integration, end-to-end)
Using maven:
`mvn clean test`

Using IDEA:
- Open the test file (or find the test directory in the project viewer)
- Right-click the desired test and press run

### Running the stress test
- Start the application (main method in `com.testing.a2z.Application`)
- Start the stress test from `testing-a2z-stress-test` directory (`mvn clean gatling:test -f pom.xml`)

## Test showcases

### 1. UserTest

`UserTest` is the first, and therefore the simplest test of the bunch. 
The `User` class has no smart logic, it just delegates password verification to the dedicated object.

![](docs/01%20-%20UserTest.svg)

There are two tests:
- `shouldInstantiateUser`
  - showcases how to create mocks and perform assertions on a result
- `shouldVerifyPassword`
  - showcases how to add behavior to mocks and check their invocations 

### 2. PasswordCharacterTypeTest

`PasswordCharacterTypeTest` shows how tests with same pre- and post-conditions can share code by utilizing parametrization.

![](docs/02%20-%20PasswordCharacterTypeTest.svg)

This way we can reduce the clutter, but we should use it sparingly.
The test will attempt to determine the character type (in the context of password symbols) for a selection of characters.

### 3. PasswordValidatorTest

`PasswordValidatorTest` displays how to assert on a piece of code that throws exceptions.

![](docs/03%20-%20PasswordValidatorTest.svg)

`PasswordValidator` class performs a validation of a plain-text password. The password needs to satisfy some rules, otherwise an exception is thrown.
Tests in the class `PasswordValidatorTest` attempt to validate different passwords, and in case of failure, make sure that the exception containing the correct reason was thrown.

### 4. HashedPasswordTest

`HashedPasswordTest` points out one possibility ("test implementation") and one issue ("non-pure static methods").

> A pure function is a function that, given the same input, will always return the same output and does not have any observable side effect.


![](docs/04%20-%20HashedPasswordTest.svg)

- `shouldCreateHashedPassword`
  - We do not always need to mock, we can instead create test implementations
  - The deprecated static method factory (`HashedPassword.create(...)`) is an example of a non-pure method
  - **Static methods can not be mocked**. Non-pure statics can hinder our test and limit what we can assert.

- `shouldSaltenPassword`
  - Tests a pure method, there are no problems testing it.
  - The only drawback is that we can not completely change the output as we can with non-static members.

### 5. HashedPasswordFactoryTest

`HashedPasswordFactoryTest` presents how to prepare the subject of the test for testing.

![](docs/05%20-%20HashedPasswordFactoryTest.svg)

Up until this test, we only created mocks and mocked behavior within the methods annotated with `@Test`.
However, usually we set up the target class and the mocks it depends on prior to the test. This test class shows how it can be done
manually.

Once again, we have a problem with password validator being static. While it does not prevent us from testing, it requires the plain-text password to be 
valid. What is considered a valid password may change in the future, and this test will need to be updated over time, even though it does not test the actual 
validation. From the perspective of the `HashedPasswordFactory`, the only important thing is whether the `PasswordValidator` deemed the password valid or not.
It would be better if we could just mock the desired behavior of the validator.

### 6. ApplicationLayerTestBase and RegisterUserUseCaseTest

These two tests show how we can configure tests in a manner that can be reused. Configuration can be shared through inheritance - `RegisterUserUseCaseTest` extends `ApplicationLayerTestBase` and
implicitly inherits all the configuration.

As an improvement to the previous test where we set up the testing subject manually, we can use Mockito with JUnit Extension API:
- `@ExtendWith(MockitoExtension.class)`
- `@Mock`
- `@InjectMocks`

JUnit also provides test lifecycle hooks, such as `@BeforeEach` or `@AfterAll`. 
The hook in this test class is used just to reassign the variable to a more appropriate name, that is not something that would normally be done.

Also, this test is written more in BDD style compared to previous ones, where BDD conventions were intentionally used more loosely.

### 7. UserControllerTest

Moving on to the integration layer, first test to take a look at is `UserControllerTest`, which sits somewhere between a unit and an integration test.
Spring Framework provides a testing library called `MockMvc`, which can be used to unit test Spring MVC `@Controllers`. We test only the HTTP part,
mocking all the dependencies that the controller uses.

![](docs/07%20-%20UserControllerTest.svg)

The setup is similar to using Mockito annotations, however Spring provides its own annotations which enable us to set up the `test context` in an even
simpler manner. The following annotations are used
- `@ExtendWith(SpringExtension.class)`
    - Adds Spring Test extension to JUnit
- `@WebMvcTest(UserController.class)`
    - Defines the scope of the test. Spring will load only the UserController when preparing the test context
- `@MockBean`
    - This annotation is similar to Mockito's `@Mock`, but will additionally enable the mock bean for autowiring and automatically reset it after each test
- `@Autowired`
    - Autowires the controller that is the subject of the test into the test class

MockMVC has its own DSL (domain-specific language), which is not in the BDD style, so the tests themselves are not written in the BDD style as well.

### 8. UserJpaRepositoryTest

`UserJpaRepositoryTest` is the first integration test in this project, meaning that it uses a real database.
Class `IntegrationTestBase` defines the shared test context for all integration and functional tests.

![](docs/08%20-%20JpaRepositoryTest.svg)

The database setup in this showcase project is as simple as possible. Alongside the test context, a small in-memory database will be started.
This is all done implicitly by Spring Data. In case we wanted to use a "real" database, we would use dockerized database via so-called Test Containers.
Infobip has a [dedicated project](https://github.com/infobip/infobip-testcontainers-spring-boot-starter) intended to help developers use popular external 
systems in testing scenarios.

There is one catch when running integration tests. In previous tests, which were unit tests, the testing configuration/context was created from scratch on every test.
However, in integration tests we need to start the whole Spring context, which is a pricey operation, so we want to start the context only once per all the tests run in a batch.
This comes out of the box when using `@SpringBootTest` annotation on our base test class, but the hidden caveat is that after a test is done, everything it wrote into
the database will remain saved. Therefore, we need to clean up the database after each test. We can use a handy snippet for this action in `IntegrationTestBase`:

```
@Autowired // NOTE: inject all repositories from the context
private List<CrudRepository<?, ?>> allRepositories;

@AfterEach
public void cleanUp() {
    allRepositories.forEach(CrudRepository::deleteAll);
}
```

The database cleanup shown above is important in order for our tests to be isolated.

### 9. UserRegistrationFunctionalTest

Functional tests (a.k.a. End-To-End, E2E) test the system from "outside", by simulating interactions that the system would encounter in production. 
All the classes in the diagram below will work in unison to handle request received via HTTP.
The functional test will, like every unit test, verify that the expected response was received - the difference is in 
the number of engaged software components that are tested at the time.

![](docs/09%20-%20UserRegistrationFunctionalTest.svg)

We can use mocks in functional tests as well, but we should strive to use as few as possible.
One other alternative is the `@SpyBean`, which is half-way between a mock and a Spring bean, allowing us to either mock
the invocation, or let the actual Spring Bean perform the operation. In the test `shouldRegisterUser`, spy mocking is used to mock a predictable userId that can be easily used in assertions.

#### Entry end of the E2E
All the `when` parts of functional tests are actually HTTP invocations. The tests make no assumptions on HTTP parsing by the server
and structure these HTTP requests as an external service would. The library used to help with preparation of these requests is RestAssured. Parts of configuration relevant for
functional tests are:
- `@SpringBootTest(webEnvironment = RANDOM_PORT)`
  - The latter part specifies that the HTTP server our application uses will be started on a random port. This technique is used to prevent port clashes on build machines.
- ```
    @BeforeEach
    public void setUp() {
        RestAssured.port = localServerPort;
    }
  ```
  - RestAssured library is normally used statically. We can set the default target port to the random port selected by the test runner.

#### Exit end of the E2E
One of the main objectives of E2E test is to make sure that any _exits_ of the application were called correctly - the database, message brokers, external HTTP endpoints that our service uses, etc.
Our service may depend on external services and we want to make sure that they are properly mocked (if needed) and that the invocations are asserted.

One common use case (not depicted in this project), would be verifying that our service has called an external HTTP endpoint. For the test to complete normally,
this HTTP endpoint must return a result. We could use classic mocking here, but such a test would not be a true End-2-End test because it would not test the critial logic
of preparing the HTTP request and parsing the HTTP response, which can both go wrong if not tested.

One common library for mocking HTTP servers is MockServer. With Spring, we would most likely use different profiles to configure production and test context differently:
- Production profile would set the external system base URL to the actual service URL, eg. `http://facebook.com`
![](docs/09%20-%20ProductionServer.svg)
- Test profile would start the HTTP service mocking library locally and set the base URL to `http://localhost:<port>`
![](docs/09%20-%20MockServer.svg)

### 10. Stress test
Stress test simulates high loads using a highly expressive DSL (domain-specific language).

The Gatling test called `StressTest` will first register a user, and the initiate a bombardment of our service with 750 requests per second for 30 seconds.

## Limitations
- DB is in-memory, for simplicity of usage


