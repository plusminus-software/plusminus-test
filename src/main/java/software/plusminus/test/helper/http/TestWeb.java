package software.plusminus.test.helper.http;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.plusminus.test.helper.TestHelper;

import java.util.Optional;

@AllArgsConstructor
@ConditionalOnWebApplication
@Component
public class TestWeb implements TestHelper {

    private Environment environment;
    private Optional<TestPageTemplate> pageTemplate;

    public int port() {
        return environment.getRequiredProperty("local.server.port", Integer.class);
    }

    public String url() {
        return "http://localhost:" + port();
    }

    public TestPageTemplate pageTemplate() {
        if (!pageTemplate.isPresent()) {
            throw new IllegalStateException("No page rest template present");
        }
        return pageTemplate.get();
    }
}
