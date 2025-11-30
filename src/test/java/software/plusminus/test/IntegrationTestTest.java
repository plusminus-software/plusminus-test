package software.plusminus.test;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.plusminus.test.helpers.context.TestContextManager;
import software.plusminus.test.helpers.data.TestDataManager;
import software.plusminus.test.helpers.rest.TestRestManager;
import software.plusminus.test.helpers.security.TestSecurityManager;

import java.util.List;
import java.util.Optional;

import static software.plusminus.check.Checks.check;

class IntegrationTestTest extends IntegrationTest {

    @Test
    void allFieldsPresent() {
        TestContextManager contextManager = context();
        checkListField(contextManager, "contexts");

        TestDataManager databaseManager = data();
        checkOptionalField(databaseManager, "databaseHelper");
        checkOptionalField(databaseManager, "databaseLog");
        checkOptionalField(databaseManager, "entityManager");
        checkOptionalField(databaseManager, "transactionHelper");

        TestRestManager restManager = rest();
        checkOptionalField(restManager, "pageRestTemplate");

        TestSecurityManager securityManager = security();
        checkField(securityManager, "securityContext");
        checkListField(securityManager, "tokenProcessors");
        checkOptionalField(securityManager, "issuerContext");
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
        check((Optional<?>) field).isPresent();
    }
}
