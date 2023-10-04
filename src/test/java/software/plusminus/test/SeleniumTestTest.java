package software.plusminus.test;

import org.junit.Test;

import static software.plusminus.check.Checks.check;

public class SeleniumTestTest extends SeleniumTest {

    @Override
    protected String url() {
        return "/";
    }

    @Test
    public void seleniumLoads() {
        String body = find("body").one().getText();
        check(body).is("Hello");
    }
}
