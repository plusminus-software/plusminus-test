package software.plusminus.test.helper.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import software.plusminus.security.service.TokenContext;
import software.plusminus.test.helper.TestHelper;

import javax.annotation.Nullable;

@ConditionalOnMissingBean(TokenContext.class)
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
