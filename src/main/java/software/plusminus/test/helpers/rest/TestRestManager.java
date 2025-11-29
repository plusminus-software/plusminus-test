package software.plusminus.test.helpers.rest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class TestRestManager {

    private Optional<TestPageRestTemplate> pageRestTemplate;

    public TestPageRestTemplate restTemplate() {
        if (!pageRestTemplate.isPresent()) {
            throw new IllegalStateException("No page rest template present");
        }
        return pageRestTemplate.get();
    }
}
