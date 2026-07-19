package software.plusminus.test.helper.security;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.authentication.service.token.HttpTokenContext;
import software.plusminus.context.Context;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenContext;
import software.plusminus.test.IntegrationTest;

import java.util.Optional;

import static software.plusminus.check.Checks.check;

public class TestSecurityTest extends IntegrationTest {

    @Autowired
    private Context<Security> securityContext;
    @Autowired
    private Optional<TestTokenContext> testTokenContext;
    @Autowired
    private Optional<TokenContext> tokenContext;

    @Test
    public void testTokenContextBacksOffWhenTokenContextIsPresent() {
        check(tokenContext.isPresent()).isTrue();
        check(tokenContext.get() instanceof HttpTokenContext).isTrue();
        check(testTokenContext.isPresent()).isFalse();
    }

    @Test
    public void loginMocksSecurityContextWhenJwtAbsent() {
        check(security().canGenerateToken()).isFalse();

        security().login(Security.builder().username("alice").build());

        Security current = securityContext.get();
        check(current).isNotNull();
        check(current.getUsername()).is("alice");
    }

    @Test
    public void clearRemovesLoggedInSecurity() {
        security().login(Security.builder().username("bob").build());

        security().clear();

        check(securityContext.optional().isPresent()).isFalse();
    }
}
