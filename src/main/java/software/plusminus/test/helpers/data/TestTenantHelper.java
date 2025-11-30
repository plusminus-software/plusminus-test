package software.plusminus.test.helpers.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;
import software.plusminus.hibernate.HibernateFilterService;
import software.plusminus.tenant.annotation.Tenant;
import software.plusminus.tenant.context.TenantContext;
import software.plusminus.test.helpers.TestHelper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ConditionalOnClass(Tenant.class)
@Component
public class TestTenantHelper implements TestHelper {

    @SpyBean
    private TenantContext tenantContext;
    @SpyBean
    private HibernateFilterService filterService;

    public void withinTenant(String tenant, Runnable runnable) {
        withinTenant(tenant, false, runnable);
    }

    public void withinTenant(String tenant, boolean ignoreHibernateFilter, Runnable runnable) {
        doReturn(tenant).when(tenantContext).get();
        if (ignoreHibernateFilter) {
            switchOffHibernateFilters();
        }
        runnable.run();
        if (ignoreHibernateFilter) {
            switchOnHibernateFilters();
        }
        doCallRealMethod().when(tenantContext).get();
    }

    public void switchOffHibernateFilters() {
        doNothing().when(filterService).enableFilters(any());
    }

    public void switchOnHibernateFilters() {
        doCallRealMethod().when(filterService).enableFilters(any());
    }
}
