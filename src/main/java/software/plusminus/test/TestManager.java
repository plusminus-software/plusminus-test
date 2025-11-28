package software.plusminus.test;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;
import software.plusminus.test.helpers.TestHelper;
import software.plusminus.test.helpers.context.TestContextManager;
import software.plusminus.test.helpers.context.TestSecurityContext;
import software.plusminus.test.helpers.database.TestDatabaseLog;
import software.plusminus.test.helpers.database.TestDatabaseManager;
import software.plusminus.test.helpers.database.TestEntityManager;
import software.plusminus.test.helpers.rest.ExtendedTestRestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class TestManager implements TestHelper {

    private Optional<TestContextManager> contextManager;
    private Optional<TestSecurityContext> securityContext;
    private Optional<TestDatabaseLog> databaseLog;
    private Optional<TestDatabaseManager> databaseManager;
    private Optional<TestEntityManager> entityManager;
    private Optional<ExtendedTestRestTemplate> restTemplate;
    private List<TokenProcessor> tokenProcessors;

    void beforeEach() {
        contextManager.ifPresent(TestContextManager::init);
    }

    void afterEach() {
        contextManager.ifPresent(TestContextManager::clear);
        databaseManager.ifPresent(TestDatabaseManager::cleanupDatabase);
        databaseLog.ifPresent(TestDatabaseLog::clear);
    }

    public <T> void context(T value) {
        if (!contextManager.isPresent()) {
            throw new IllegalStateException("No Context<?> is present");
        }
        contextManager.get().setValue(value);
    }

    public String lastSqlQuery() {
        if (!databaseLog.isPresent()) {
            throw new IllegalStateException("No database log is present");
        }
        return databaseLog.get().getLastSql();
    }

    public TestEntityManager entityManager() {
        if (!entityManager.isPresent()) {
            throw new IllegalStateException("No entity manager is present");
        }
        return entityManager.get();
    }

    public ExtendedTestRestTemplate restTemplate() {
        if (!restTemplate.isPresent()) {
            throw new IllegalStateException("No test rest template is present");
        }
        return restTemplate.get();
    }

    public void security(Security security) {
        if (!securityContext.isPresent()) {
            throw new IllegalStateException("No security context is present");
        }
        securityContext.ifPresent(s -> s.withSecurity(security));
    }

    public String generateToken(Security security) {
        return tokenProcessors.stream()
                .map(tokenProcessor -> tokenProcessor.getToken(security))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
