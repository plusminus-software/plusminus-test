package software.plusminus.test;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import software.plusminus.test.helper.TestConfiguration;
import software.plusminus.test.helper.context.TestContext;
import software.plusminus.test.helper.data.TestData;
import software.plusminus.test.helper.http.TestWeb;
import software.plusminus.test.helper.security.TestSecurity;

import java.util.Optional;

@SuppressWarnings("java:S6813")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "integration-test"})
@Import(TestConfiguration.class)
public abstract class IntegrationTest {

    @Autowired
    private Optional<TestContext> context;
    @Autowired
    private Optional<TestSecurity> security;
    @Autowired
    private Optional<TestWeb> web;
    @Autowired
    private Optional<TestData> data;

    @After
    @AfterEach
    public void afterEach() {
        context.ifPresent(TestContext::clear);
        security.ifPresent(TestSecurity::clear);
        data.ifPresent(TestData::clear);
    }

    public TestContext context() {
        return context.orElseThrow(() -> new IllegalStateException("No context module present"));
    }

    public TestSecurity security() {
        return security.orElseThrow(() -> new IllegalStateException("No security module present"));
    }

    public TestWeb web() {
        return web.orElseThrow(() -> new IllegalStateException("No web module present"));
    }

    public TestData data() {
        return data.orElseThrow(() -> new IllegalStateException("No data module present"));
    }
}
