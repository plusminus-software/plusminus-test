package software.plusminus.test.helpers.database;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.plusminus.test.helpers.TestHelper;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TestDatabaseManager implements TestHelper {

    private Optional<TestDatabaseHelper> databaseHelper;
    private Optional<TestDatabaseLog> databaseLog;
    private Optional<TestEntityManager> entityManager;
    private Optional<TestTransactionHelper> transactionHelper;

    public void cleanup() {
        databaseHelper.ifPresent(TestDatabaseHelper::cleanupDatabase);
        databaseLog.ifPresent(TestDatabaseLog::clear);
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

    public TestTransactionHelper transaction() {
        if (!transactionHelper.isPresent()) {
            throw new IllegalStateException("No transaction module is present");
        }
        return transactionHelper.get();
    }
}
