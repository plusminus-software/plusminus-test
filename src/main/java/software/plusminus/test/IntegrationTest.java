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

@SuppressWarnings("java:S6813")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "integration-test"})
@Import(TestConfiguration.class)
public abstract class IntegrationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestManager manager;

    @Before
    @BeforeEach
    public void beforeEach() {
        manager.beforeEach();
    }

    @After
    @AfterEach
    public void afterEach() {
        manager.afterEach();
    }

    protected int port() {
        return port;
    }

    protected String url() {
        return "http://localhost:" + port;
    }

    protected TestManager manager() {
        return manager;
    }
}
