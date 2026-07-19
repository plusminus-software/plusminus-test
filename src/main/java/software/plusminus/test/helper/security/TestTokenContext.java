package software.plusminus.test.helper.security;

import software.plusminus.security.service.TokenContext;
import software.plusminus.test.helper.TestHelper;

import javax.annotation.Nullable;

/**
 * Default fallback {@link TokenContext} used in integration tests when the application
 * under test does not provide its own. Registered as a bean via
 * {@link software.plusminus.test.helper.TestConfiguration#testTokenContext()} with
 * {@code @ConditionalOnMissingBean(TokenContext.class)}, so it backs off whenever any
 * {@code TokenContext} is already present — e.g. {@code HttpTokenContext} contributed by
 * the plusminus-authentication module.
 */
public class TestTokenContext implements TokenContext, TestHelper {

    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    @Nullable
    @Override
    public String getToken() {
        return TOKEN.get();
    }

    @Override
    public boolean setToken(String token) {
        TOKEN.set(token);
        return true;
    }

    @Override
    public void clearToken() {
        TOKEN.remove();
    }
}
