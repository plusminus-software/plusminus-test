package software.plusminus.test.helper.data.tenant;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static software.plusminus.check.Checks.check;

public class ThreadLocalTestTenantTest {

    private final ThreadLocalTestTenant tenant = new ThreadLocalTestTenant();

    @Test
    public void noTenantUntilUsed() {
        check(tenant.currentTenant()).isNull();
    }

    @Test
    public void useSetsAndClearRemovesTenant() {
        tenant.use("some tenant");
        check(tenant.currentTenant()).is("some tenant");

        tenant.clear();
        check(tenant.currentTenant()).isNull();
    }

    @Test
    public void tenantIsBoundToTheCallingThread() throws InterruptedException {
        tenant.use("some tenant");
        AtomicReference<String> other = new AtomicReference<>("not read");

        Thread thread = new Thread(() -> other.set(tenant.currentTenant()));
        thread.start();
        thread.join();

        check(other.get()).isNull();
        tenant.clear();
    }
}
