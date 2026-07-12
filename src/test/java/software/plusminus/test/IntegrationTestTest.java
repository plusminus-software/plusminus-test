package software.plusminus.test;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.plusminus.test.helper.context.TestContext;
import software.plusminus.test.helper.data.TestData;
import software.plusminus.test.helper.http.TestWeb;
import software.plusminus.test.helper.security.TestSecurity;

import java.util.List;
import java.util.Optional;

import static software.plusminus.check.Checks.check;

class IntegrationTestTest extends IntegrationTest {

    @Test
    void allFieldsPresent() {
        TestContext contextManager = context();
        checkListField(contextManager, "contexts");

        TestData databaseManager = data();
        checkOptionalField(databaseManager, "database");
        checkOptionalField(databaseManager, "log");
        checkOptionalField(databaseManager, "transaction");

        TestWeb webManager = web();
        checkOptionalField(webManager, "pageTemplate");

        TestSecurity securityManager = security();
        checkField(securityManager, "securityService");
        checkField(securityManager, "tokenProcessors");
    }

    private void checkField(Object targetObject, String fieldName) {
        Object field = ReflectionTestUtils.getField(targetObject, fieldName);
        check(field).isNotNull();
    }

    private void checkListField(Object targetObject, String fieldName) {
        Object field = ReflectionTestUtils.getField(targetObject, fieldName);
        check(field).isNotNull();
        check((List<?>) field).isNotEmpty();
    }

    private void checkOptionalField(Object targetObject, String fieldName) {
        Object field = ReflectionTestUtils.getField(targetObject, fieldName);
        check(field).isNotNull();
        check((Optional<?>) field).isNotEmpty();
    }
}
