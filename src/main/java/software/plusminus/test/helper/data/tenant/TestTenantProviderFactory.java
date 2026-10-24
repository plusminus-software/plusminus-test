package software.plusminus.test.helper.data.tenant;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * Registers {@link ThreadLocalTestTenant} as a highest-priority {@code TenantProvider}.
 *
 * <p>{@code plusminus-test} does not depend on {@code plusminus-tenant}, so the provider is a proxy created
 * for the {@code TenantProvider} interface found on the classpath.
 */
@AllArgsConstructor
@ConditionalOnClass(name = TestTenantProviderFactory.TENANT_PROVIDER)
@Component
public class TestTenantProviderFactory implements FactoryBean<Object> {

    static final String TENANT_PROVIDER = "software.plusminus.tenant.service.TenantProvider";

    private final ThreadLocalTestTenant tenant;

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{getObjectType(), Ordered.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "currentTenant":
                            return tenant.currentTenant();
                        case "getOrder":
                            return Ordered.HIGHEST_PRECEDENCE;
                        case "hashCode":
                            return System.identityHashCode(proxy);
                        case "equals":
                            return proxy == args[0];
                        case "toString":
                            return "ThreadLocalTestTenantProvider";
                        default:
                            throw new UnsupportedOperationException(method.toString());
                    }
                });
    }

    @Override
    public Class<?> getObjectType() {
        return providerType();
    }

    Class<?> providerType() {
        try {
            return Class.forName(TENANT_PROVIDER, false, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
