package software.plusminus.test.helper.data.tenant;

import org.junit.Test;
import org.springframework.core.Ordered;
import software.plusminus.tenant.service.TenantProvider;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static software.plusminus.check.Checks.check;

public class TestTenantProviderFactoryTest {

    private final ThreadLocalTestTenant tenant = new ThreadLocalTestTenant();
    private final TestTenantProviderFactory factory = new TestTenantProviderFactory(tenant) {
        @Override
        Class<?> providerType() {
            return Provider.class;
        }
    };

    @Test
    public void objectTypeIsTheProviderInterface() {
        check(factory.getObjectType() == Provider.class).isTrue();
    }

    @Test
    public void proxyIsProviderAndOrdered() {
        Object proxy = factory.getObject();

        check(proxy instanceof Provider).isTrue();
        check(proxy instanceof Ordered).isTrue();
        check(((Ordered) proxy).getOrder()).is(Ordered.HIGHEST_PRECEDENCE);
    }

    @Test
    public void proxyReportsTenantOnlyAfterUse() {
        Provider provider = (Provider) factory.getObject();
        check(provider.currentTenant()).isNull();

        tenant.use("some tenant");
        check(provider.currentTenant()).is("some tenant");

        tenant.clear();
        check(provider.currentTenant()).isNull();
    }

    @Test
    public void proxyHasIdentityEqualsHashCodeAndToString() {
        Object proxy = factory.getObject();

        check(proxy.equals(proxy)).isTrue();
        check(proxy.equals(factory.getObject())).isFalse();
        check(proxy.hashCode()).is(System.identityHashCode(proxy));
        check(proxy.toString()).is("ThreadLocalTestTenantProvider");
    }

    @Test
    public void unknownMethodIsNotSupported() {
        Provider provider = (Provider) factory.getObject();

        assertThatThrownBy(provider::unsupported).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void defaultProviderTypeIsLookedUpByName() {
        check(new TestTenantProviderFactory(tenant).getObjectType() == TenantProvider.class).isTrue();
    }

    public interface Provider {
        String currentTenant();

        void unsupported();
    }
}
