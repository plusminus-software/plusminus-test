package software.plusminus.test.helper.data;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import software.plusminus.test.helper.TestHelper;
import software.plusminus.test.helper.data.log.TestDatabaseLog;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TestData implements TestHelper {

    private Optional<TestDatabase> database;
    private Optional<TestDatabaseLog> log;
    private Optional<TestTransaction> transaction;

    public void clear() {
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
}
