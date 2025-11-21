package software.plusminus.test.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;

public interface TestHelper {

    @Autowired
    default void foolproof(Environment environment) {
        boolean isTestEnvironment = Arrays.asList(environment.getActiveProfiles()).contains("test");
        if (!isTestEnvironment) {
            throw new IllegalStateException("TestHelper must run only in the scope of 'test' profile");
        }
    }
}
