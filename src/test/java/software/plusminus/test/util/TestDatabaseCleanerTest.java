package software.plusminus.test.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.test.IntegrationTest;
import software.plusminus.test.fixtures.TestEntity;

import static software.plusminus.check.Checks.check;

public class TestDatabaseCleanerTest extends IntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void first() {
        checkDbIsEmpty();
        create(1L);
        create(2L);
    }

    @Test
    public void second() {
        checkDbIsEmpty();
        create(1L);
        create(2L);
        create(3L);
    }

    private void create(Long expectedId) {
        TestEntity entity = new TestEntity();
        entity.setStringField("my string " + expectedId);
        entityManager.persist(entity);

        check(entity.getId()).is(expectedId);
        check(entityManager.find(TestEntity.class, expectedId)).isNotNull();
        check(entityManager.find(TestEntity.class, expectedId + 1)).isNull();
    }

    private void checkDbIsEmpty() {
        check(entityManager.find(TestEntity.class, 1L)).isNull();
    }
}