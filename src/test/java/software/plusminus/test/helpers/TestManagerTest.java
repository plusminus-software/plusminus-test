package software.plusminus.test.helpers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import software.plusminus.test.IntegrationTest;
import software.plusminus.test.TestManager;

import java.util.Optional;

import static software.plusminus.check.Checks.check;

public class TestManagerTest extends IntegrationTest {

    @Autowired
    private TestManager testManager;

    @Test
    public void allFieldsPresent() {
        checkOptionalField("contextManager");
        checkOptionalField("databaseLog");
        checkOptionalField("databaseManager");
        checkOptionalField("entityManager");
        checkOptionalField("restTemplate");
    }

    private void checkOptionalField(String fieldName) {
        Object field = ReflectionTestUtils.getField(testManager, fieldName);
        check(field).isNotNull();
        check((Optional<?>) field).isPresent();
    }
}