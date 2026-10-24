package software.plusminus.test.helper.data.tenant;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.tenant.service.TenantProvider;

@Order(1)
@Component
public class ApplicationTenantProvider implements TenantProvider {

    private volatile String tenant;

    public void set(@Nullable String newTenant) {
        this.tenant = newTenant;
    }

    @Nullable
    @Override
    public String currentTenant() {
        return tenant;
    }
}
