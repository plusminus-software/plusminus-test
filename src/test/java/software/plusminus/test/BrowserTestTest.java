package software.plusminus.test;

import org.junit.Test;
import software.plusminus.selenium.model.WebTestOptions;

import static software.plusminus.check.Checks.check;

public class BrowserTestTest extends BrowserTest {

    @Override
    public void beforeEach() {
        data().tenant().switchOffHibernateFilters();
        super.beforeEach();
    }

    @Override
    protected WebTestOptions options() {
        return super.options()
                .logsFilter(log -> !log.getMessage().contains("favicon"));
    }

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
