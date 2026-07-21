package software.plusminus.test;

import org.junit.Test;
import software.plusminus.browser.BrowserSettings;

import static software.plusminus.check.Checks.check;

public class BrowserTestTest extends BrowserTest {

    @Override
    protected BrowserSettings settings() {
        return super.settings()
                .logsFilter(message -> !message.contains("favicon"));
    }

    @Test
    public void seleniumLoads() {
        go("/");
        String body = find("body").one().text();
        check(body).is("Hello");
    }
}
