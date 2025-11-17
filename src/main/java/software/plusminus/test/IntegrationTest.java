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
import software.plusminus.context.Context;
import software.plusminus.test.util.TestDatabaseCleaner;
import software.plusminus.test.util.TestDatabaseLog;
import software.plusminus.test.util.TestEntityManager;
import software.plusminus.test.util.TestRestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "integration-test"})
@Import({TestDatabaseCleaner.class, TestDatabaseLog.class,
         TestEntityManager.class, TestRestTemplate.class})
public abstract class IntegrationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestDatabaseCleaner databaseCleaner;

    @Before
    @BeforeEach
    public void initContext() {
        Context.init();
    }

    @After
    @AfterEach
    public void clearContext() {
        Context.clear();
    }

    @After
    @AfterEach
    public void cleanupDatabase() {
        databaseCleaner.cleanupDatabase();
        TestDatabaseLog.clear();
    }

    protected int port() {
        return port;
    }

    protected String url() {
        return "http://localhost:" + port;
    }
}
