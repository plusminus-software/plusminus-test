package software.plusminus.tenant.service;

import javax.annotation.Nullable;

/**
 * Stands in for the {@code TenantProvider} of {@code plusminus-tenant}, which {@code plusminus-test} does not
 * depend on. It lets the tenant test helper be wired and tested in this module.
 */
public interface TenantProvider {

    @Nullable
    String currentTenant();
}
