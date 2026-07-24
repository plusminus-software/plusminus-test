# plusminus-test

Base classes and helpers for writing Spring Boot integration and browser tests.

The library provides two abstract base classes — `IntegrationTest` and `BrowserTest` — plus a set of
helper modules that are auto-discovered from the Spring context. Tests can use both JUnit 4 and
JUnit 5 lifecycles: the base classes carry both sets of annotations.

## IntegrationTest

`IntegrationTest` bootstraps a full `@SpringBootTest` on a random port with the `test` and
`integration-test` profiles active, and imports `TestConfiguration` (a component scan over
`software.plusminus.test`). Helper modules are exposed through accessor methods:

- `context()` — `TestContext`: sets and clears `plusminus-context` contexts
- `security()` — `TestSecurity`: logs a user in, by token or by mocking `SecurityService`
- `web()` — `TestWeb`: server `port()`, base `url()`, `pageTemplate()`
- `data()` — `TestData`: database cleanup, `log()`, `transaction()`

Tests run unauthenticated by default — call `security().login(username, roles...)`
explicitly when a test needs an authenticated user. After each test the
context, security and data modules are cleared automatically.

```java
public class ServerTest extends IntegrationTest {

    @Test
    public void serverStarts() {
        check(web().url()).is("http://localhost:" + web().port());
    }
}
```

Each module is optional: if its beans are not present, the accessor throws `IllegalStateException`
only when actually called.

## BrowserTest

`BrowserTest` extends `IntegrationTest` and drives a Selenium browser (via `plusminus-selenium`).
One browser instance is shared per test class and closed after the class; the browser is created
before each test and its cookies are cleared after each test. There is no automatic navigation —
call `go(page)` to navigate to a page. Useful methods: `find(...)`, `browser()`, `go(page)`,
`login(username, roles...)` and an overridable `settings()`. Annotate the test class with
`@TestBrowser(headless = true)` to run headless.

```java
public class HomePageTest extends BrowserTest {

    @Test
    public void rendersGreeting() {
        go("/");
        check(find("body").one().text()).is("Hello");
    }
}
```

### Optional security integration

Security helpers activate only when the optional `plusminus-security-core` dependency is on the
test classpath: `TestSecurity` and `TestSecurityConfiguration` are guarded by `@ConditionalOnClass`,
so projects without it work out of the box and need no extra dependencies. For the same reason
`BrowserTest.login(username, roles...)` deliberately keeps security classes out of method
signatures, so JUnit can reflect over the test class without them.

## Security note on the bundled test keys

This module ships throwaway RSA key pairs (`test_private_key.pem`, `test_private_key_pkcs8.pem`,
`test_public_key.pem`) as classpath resources, and the `integration-test` profile wires the private
key as the JWT signing key (`plusminus.jwt.privateKey`). These keys are committed to source control
and therefore **publicly known** — they exist only so integration and browser tests can mint and
verify JWTs deterministically.

Because plusminus-test is meant to be depended on with `<scope>test</scope>`, these resources should
only ever reach a project's test classpath. **Never enable the `test` or `integration-test` Spring
profiles in a production (or any internet-facing) deployment**, and never reuse these keys for
anything real — doing so would let anyone forge valid tokens. Production deployments must supply
their own private key via `plusminus.jwt.privateKey`.

## Getting started

```xml
<dependency>
    <groupId>software.plusminus</groupId>
    <artifactId>plusminus-test</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

## Building

Requires JDK 8:

```bash
./mvnw clean install
```

The build enforces Checkstyle, PMD, SpotBugs and JaCoCo coverage checks. Surefire forks one JVM
per CPU core (`forkCount=1C`), so each fork gets an isolated database, Spring context and browser.

## License

[Apache License 2.0](LICENSE)
