package software.plusminus.test.helper.data;

import org.junit.Test;
import software.plusminus.test.helper.data.log.TestDatabaseLog;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static software.plusminus.check.Checks.check;

public class TestDataTest {

    @Test
    public void returnsPresentHelpersAndClears() {
        TestDatabase database = mock(TestDatabase.class);
        TestDatabaseLog log = mock(TestDatabaseLog.class);
        TestTransaction transaction = new TestTransaction();
        TestData data = new TestData(Optional.of(database), Optional.of(log), Optional.of(transaction));

        check(data.log() == log).isTrue();
        check(data.transaction() == transaction).isTrue();

        data.clear();
        verify(database).clear();
        verify(log).clear();
    }

    @Test
    public void clearWithoutHelpersDoesNothing() {
        new TestData(Optional.empty(), Optional.empty(), Optional.empty()).clear();
    }

    @Test(expected = IllegalStateException.class)
    public void logThrowsWhenAbsent() {
        new TestData(Optional.empty(), Optional.empty(), Optional.empty()).log();
    }

    @Test(expected = IllegalStateException.class)
    public void transactionThrowsWhenAbsent() {
        new TestData(Optional.empty(), Optional.empty(), Optional.empty()).transaction();
    }
}
