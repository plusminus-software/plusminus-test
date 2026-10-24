package software.plusminus.test.helper.data.tenant;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.tenant.service.TenantProvider;
import software.plusminus.test.IntegrationTest;

import java.util.List;
import java.util.Objects;

import static software.plusminus.check.Checks.check;

public class TestTenantTest extends IntegrationTest {

    @Autowired
    private List<TenantProvider> providers;
    @Autowired
    private ApplicationTenantProvider application;

    @After
    public void resetApplicationTenant() {
        application.set(null);
    }

    @Test
    public void noTenantWhenNothingProvidesOne() {
        check(currentTenant()).isNull();
    }

    @Test
    public void applicationProvidersResolveTenantWhenTestDoesNotUseOne() {
        application.set("tenant from application");

        check(currentTenant()).is("tenant from application");
    }

    @Test
    public void usedTenantWinsOverApplicationProviders() {
        application.set("tenant from application");

        data().tenant().use("tenant from test");

        check(currentTenant()).is("tenant from test");
    }

    @Test
    public void clearFallsBackToApplicationProvidersAgain() {
        application.set("tenant from application");
        data().tenant().use("tenant from test");

        data().tenant().clear();

        check(currentTenant()).is("tenant from application");
    }

    @Test
    public void testTenantProviderIsFirst() {
        check(providers.get(0) == application).isFalse();
    }

    private String currentTenant() {
        return providers.stream()
                .map(TenantProvider::currentTenant)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
