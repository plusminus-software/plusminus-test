package software.plusminus.test.helper.data.tenant;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.security.Security;
import software.plusminus.tenant.context.TenantContext;
import software.plusminus.test.IntegrationTest;

import java.util.Collections;

import static software.plusminus.check.Checks.check;

public class TestTenantTest extends IntegrationTest {

    @Autowired
    private TenantContext tenantContext;

    @Test
    public void noTenantWhenNothingProvidesOne() {
        check(tenantContext.get()).isNull();
    }

    @Test
    public void applicationProvidersResolveTenantWhenTestDoesNotUseOne() {
        loginWithTenant("tenant from security context");

        check(tenantContext.get()).is("tenant from security context");
    }

    @Test
    public void usedTenantWinsOverApplicationProviders() {
        loginWithTenant("tenant from security context");

        data().tenant().use("tenant from test");

        check(tenantContext.get()).is("tenant from test");
    }

    @Test
    public void clearFallsBackToApplicationProvidersAgain() {
        loginWithTenant("tenant from security context");
        data().tenant().use("tenant from test");

        data().tenant().clear();

        check(tenantContext.get()).is("tenant from security context");
    }

    private void loginWithTenant(String tenant) {
        security().login(Security.builder()
                .username("alice")
                .parameters(Collections.singletonMap("tenant", tenant))
                .build());
    }
}
