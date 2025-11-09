package software.plusminus.test.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.test.IntegrationTest;
import software.plusminus.test.fixtures.TestEntity;

import static software.plusminus.check.Checks.check;

public class DatabaseLogTest extends IntegrationTest {

    @Autowired
    private TransactionEntityManager entityManager;

    @Test
    public void getLastSql() {
        TestEntity entity = new TestEntity();
        entity.setStringField("my string");
        entityManager.persist(entity);

        String lastSql = DatabaseLog.getLastSql();

        check(lastSql).is("insert into test_entity (id, string_field) values (default, ?)");
    }

    @Test
    public void explainLastSql() {
        TestEntity entity = new TestEntity();
        entity.setStringField("my string");
        entityManager.persist(entity);

        entityManager.find(TestEntity.class, 1L);
        String explainedLastSql = DatabaseLog.explainLastSql(1L);

        check(explainedLastSql.contains("EXPLAIN")).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void explainLastSql_incorrectParameters() {
        TestEntity entity = new TestEntity();
        entity.setStringField("my string");
        entityManager.persist(entity);

        entityManager.find(TestEntity.class, 1L);
        DatabaseLog.explainLastSql(1L, 2L);
    }
}