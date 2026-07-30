package software.plusminus.test.helper.data.tenant;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.tenant.service.TenantProvider;

/**
 * Sets the current tenant for the running test.
 *
 * <p>Registers itself as the highest-priority {@link TenantProvider}, but only reports a tenant
 * once a test has called {@link #use(String)}. Until then it reports none, so the tenant keeps
 * being resolved by the providers the application registers itself — from the security context,
 * the request URL and so on. From the call on, the value the test set wins over all of them.
 *
 * <p>The value is bound to the thread that sets it: data access from the test itself sees it,
 * while requests the application serves on its own threads keep resolving their tenant as they do
 * in production.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(TenantProvider.class)
@Component
public class ThreadLocalTestTenant implements TestTenant, TenantProvider {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    @Override
    public void use(String tenant) {
        currentTenant.set(tenant);
    }

    @Nullable
    @Override
    public String currentTenant() {
        return currentTenant.get();
    }

    @Override
    public void clear() {
        currentTenant.remove();
    }
}
