package software.plusminus.test.helper.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;
import software.plusminus.authentication.service.token.HttpTokenContext;
import software.plusminus.browser.BrowserCookies;
import software.plusminus.security.Security;
import software.plusminus.security.service.SecurityService;
import software.plusminus.security.service.TokenProcessor;
import software.plusminus.test.helper.TestHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ConditionalOnClass(Security.class)
@Component
public class TestSecurity implements TestHelper {

    @SpyBean
    private SecurityService securityService;
    private final List<TokenProcessor> tokenProcessors;
    private final List<TestTokenContext> tokenContexts;

    public void login(String username, String... roles) {
        login(Security.builder()
                .username(username)
                .roles(new HashSet<>(Arrays.asList(roles)))
                .build());
    }

    public void login(Security security) {
        if (canGenerateToken()) {
            String token = generateToken(security);
            tokenContexts.forEach(context -> context.setToken(token));
        } else {
            when(securityService.getSecurity()).thenReturn(security);
        }
    }

    public void login(BrowserCookies cookies, String username, String... roles) {
        login(cookies, Security.builder()
                .username(username)
                .roles(new HashSet<>(Arrays.asList(roles)))
                .build());
    }

    public void login(BrowserCookies cookies, Security security) {
        if (canGenerateToken()) {
            cookies.add(HttpTokenContext.COOKIE_NAME, generateToken(security));
        } else {
            login(security);
        }
    }

    public boolean canGenerateToken() {
        return !tokenProcessors.isEmpty();
    }

    public String generateToken(Security security) {
        return tokenProcessors.stream()
                .map(tokenProcessor -> tokenProcessor.getToken(security))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void clear() {
        reset(securityService);
        tokenContexts.forEach(TestTokenContext::clearToken);
    }
}
