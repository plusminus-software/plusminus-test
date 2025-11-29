package software.plusminus.test;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.test.helpers.TestConfiguration;
import software.plusminus.test.helpers.context.TestContextManager;
import software.plusminus.test.helpers.database.TestDatabaseManager;
import software.plusminus.test.helpers.rest.TestRestManager;
import software.plusminus.test.helpers.security.TestSecurityManager;

import java.util.Optional;

@SuppressWarnings("java:S6813")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "integration-test"})
@Import(TestConfiguration.class)
public abstract class IntegrationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private Optional<TestContextManager> contextManager;
    @Autowired
    private Optional<TestDatabaseManager> databaseManager;
    @Autowired
    private Optional<TestRestManager> restManager;
    @Autowired
    private Optional<TestSecurityManager> securityManager;

    @Before
    @BeforeEach
    public void beforeEach() {
        contextManager.ifPresent(TestContextManager::init);
    }

    @After
    @AfterEach
    public void afterEach() {
        contextManager.ifPresent(TestContextManager::clear);
        databaseManager.ifPresent(TestDatabaseManager::cleanup);
    }

    protected int port() {
        return port;
    }

    protected String url() {
        return "http://localhost:" + port;
    }

    public TestContextManager context() {
        return contextManager
                .orElseThrow(() -> new IllegalStateException("No context module present"));
    }

    public TestDatabaseManager database() {
        return databaseManager
                .orElseThrow(() -> new IllegalStateException("No data module present"));
    }

    public TestRestManager rest() {
        return restManager
                .orElseThrow(() -> new IllegalStateException("No rest module present"));
    }

    public TestSecurityManager security() {
        return securityManager
                .orElseThrow(() -> new IllegalStateException("No security module present"));
    }
}
