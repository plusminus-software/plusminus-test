package software.plusminus.test.helpers.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;
import software.plusminus.context.Context;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;
import software.plusminus.test.helpers.TestHelper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ConditionalOnClass(Security.class)
@Component
public class TestSecurityManager implements TestHelper {

    @SpyBean
    private Context<Security> securityContext;
    private final List<TokenProcessor> tokenProcessors;
    private final Optional<TestIssuerContext> issuerContext;

    public void setContext(Security security) {
        when(securityContext.get()).thenReturn(security);
        when(securityContext.optional()).thenReturn(Optional.of(security));
    }

    public String generateToken(Security security) {
        return tokenProcessors.stream()
                .map(tokenProcessor -> tokenProcessor.getToken(security))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public String generateToken(Security security, String issuer) {
        if (!issuerContext.isPresent()) {
            throw new IllegalStateException();
        }
        issuerContext.get().set(issuer);
        return generateToken(security);
    }
}
