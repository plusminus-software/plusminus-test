package software.plusminus.test.helpers.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;
import software.plusminus.jwt.service.IssuerContext;

import static org.mockito.Mockito.when;

@ConditionalOnClass(IssuerContext.class)
@Component
public class TestIssuerContext {

    @SpyBean
    private IssuerContext issuerContext;

    public void set(String issuer) {
        when(issuerContext.get()).thenReturn(issuer);
    }
}
