package software.plusminus.test.helper.security;

import org.junit.Test;

import static software.plusminus.check.Checks.check;

public class TestTokenContextTest {

    private final TestTokenContext tokenContext = new TestTokenContext();

    @Test
    public void setGetAndClearToken() {
        check(tokenContext.getToken()).isNull();

        boolean set = tokenContext.setToken("my-token");
        check(set).isTrue();
        check(tokenContext.getToken()).is("my-token");

        tokenContext.clearToken();
        check(tokenContext.getToken()).isNull();
    }
}
