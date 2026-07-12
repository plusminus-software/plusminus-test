package software.plusminus.test.helper.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.test.IntegrationTest;
import software.plusminus.test.fixtures.TestEntity;
import software.plusminus.test.helper.data.log.HibernateTestDatabaseLog;

import static software.plusminus.check.Checks.check;

public class HibernateTestDatabaseLogTest extends IntegrationTest {

    @Autowired
    private HibernateTestDatabaseLog databaseLog;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getLastSql() {
        TestEntity entity = new TestEntity();
        entity.setStringField("my string");
        entityManager.persist(entity);

        String lastSql = databaseLog.getLastSql();

        check(lastSql).is("insert into test_entity (id, string_field) values (null, ?)");
    }
}