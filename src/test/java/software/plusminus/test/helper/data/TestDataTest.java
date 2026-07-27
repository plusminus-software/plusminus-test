package software.plusminus.test.helper.data;

import org.junit.Test;
import software.plusminus.test.helper.data.log.TestDatabaseLog;
import software.plusminus.test.helper.data.tenant.TestTenant;

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
        TestTenant tenant = new TestTenant();
        TestData data = new TestData(Optional.of(database), Optional.of(log), Optional.of(transaction),
                Optional.of(tenant));
        tenant.use("some tenant");

        check(data.log() == log).isTrue();
        check(data.transaction() == transaction).isTrue();
        check(data.tenant() == tenant).isTrue();

        data.clear();
        verify(database).clear();
        verify(log).clear();
        check(tenant.currentTenant()).isNull();
    }

    @Test
    public void clearWithoutHelpersDoesNothing() {
        empty().clear();
    }

    @Test(expected = IllegalStateException.class)
    public void logThrowsWhenAbsent() {
        empty().log();
    }

    @Test(expected = IllegalStateException.class)
    public void transactionThrowsWhenAbsent() {
        empty().transaction();
    }

    @Test(expected = IllegalStateException.class)
    public void tenantThrowsWhenAbsent() {
        empty().tenant();
    }

    private TestData empty() {
        return new TestData(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }
}
