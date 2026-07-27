package software.plusminus.test.helper.data;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.plusminus.test.helper.TestHelper;
import software.plusminus.test.helper.data.log.TestDatabaseLog;
import software.plusminus.test.helper.data.tenant.TestTenant;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TestData implements TestHelper {

    private Optional<TestDatabase> database;
    private Optional<TestDatabaseLog> log;
    private Optional<TestTransaction> transaction;
    private Optional<TestTenant> tenant;

    public void clear() {
        tenant.ifPresent(TestTenant::clear);
        database.ifPresent(TestDatabase::clear);
        log.ifPresent(TestDatabaseLog::clear);
    }

    public TestDatabaseLog log() {
        if (!log.isPresent()) {
            throw new IllegalStateException("No database log is present");
        }
        return log.get();
    }

    public TestTransaction transaction() {
        if (!transaction.isPresent()) {
            throw new IllegalStateException("No transaction module is present");
        }
        return transaction.get();
    }

    public TestTenant tenant() {
        if (!tenant.isPresent()) {
            throw new IllegalStateException("No tenant module is present");
        }
        return tenant.get();
    }
}
