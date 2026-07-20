package software.plusminus.test.helper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("software.plusminus.test")
public class TestConfiguration implements TestHelper {
}
