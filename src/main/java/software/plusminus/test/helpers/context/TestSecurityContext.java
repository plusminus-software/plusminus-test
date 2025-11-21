package software.plusminus.test.helpers.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;
import software.plusminus.context.Context;
import software.plusminus.security.Security;
import software.plusminus.test.helpers.TestHelper;

import static org.mockito.Mockito.when;

@ConditionalOnClass(Security.class)
@Component
public class TestSecurityContext implements TestHelper {

    @SpyBean
    private Context<Security> securityContext;

    public void withSecurity(Security security) {
        when(securityContext.get()).thenReturn(security);
    }
}
