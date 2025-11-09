package software.plusminus.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.context.Context;
import software.plusminus.test.util.DatabaseCleaner;
import software.plusminus.test.util.DatabaseLog;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles({ "test", "integration-test" })
public abstract class IntegrationTest {
    
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Before
    public void initContext() {
        Context.init();
    }

    @After
    public void clearContext() {
        Context.clear();
    }

    @After
    public void cleanupDatabase() {
        databaseCleaner.cleanupDatabase();
        DatabaseLog.clear();
    }

    protected int port() {
        return port;
    }
}
