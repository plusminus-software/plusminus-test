package software.plusminus.test.helper.security;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.context.Context;
import software.plusminus.security.Security;
import software.plusminus.test.IntegrationTest;

import java.util.Optional;

import static software.plusminus.check.Checks.check;

public class TestSecurityTest extends IntegrationTest {

    @Autowired
    private Context<Security> securityContext;
    @Autowired
    private Optional<TestTokenContext> tokenContext;

    @Test
    public void tokenContextIsRegisteredAsFallbackBean() {
        check(tokenContext.isPresent()).isTrue();
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
