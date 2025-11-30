package software.plusminus.test.helpers.rest;

import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import software.plusminus.test.helpers.TestHelper;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TestRestManager implements TestHelper {

    private Optional<TestRestTemplate> restTemplate;
    private Optional<TestPageRestTemplate> pageRestTemplate;

    public TestRestTemplate restTemplate() {
        if (!restTemplate.isPresent()) {
            throw new IllegalStateException("No rest template present");
        }
        return restTemplate.get();
    }

    public TestPageRestTemplate pageRestTemplate() {
        if (!pageRestTemplate.isPresent()) {
            throw new IllegalStateException("No page rest template present");
        }
        return pageRestTemplate.get();
    }
}
