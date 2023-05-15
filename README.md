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
TODO
Spring MockMvc, non BDD
Unit tests for HTTP

### 8. UserRegistrationFunctionalTest and UserVerificationFunctionalTest
TODO
showcase @SpyBean, end to end test example which will take the most into testing context

### 9. Stress test
TODO
Stress test simulates high loads using a highly expressive DSL (domain-specific language).

## Limitations
- DB is in-memory, for simplicity of usage


