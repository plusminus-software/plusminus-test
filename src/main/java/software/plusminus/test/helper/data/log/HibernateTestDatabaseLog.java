package software.plusminus.test.helper.data.log;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.annotation.Nullable;

@ConditionalOnClass(StatementInspector.class)
@Component
public class HibernateTestDatabaseLog implements TestDatabaseLog, StatementInspector {

    /*
     * Shared across threads (and across the two instances that exist at runtime: the Spring bean
     * that tests autowire, and the separate instance Hibernate creates as its StatementInspector).
     * A previous ThreadLocal implementation captured SQL keyed by the executing thread, so for
     * RANDOM_PORT integration tests - where Hibernate runs on servlet worker threads - the SQL was
     * invisible to the reading test thread. A static, thread-safe deque makes capture thread-agnostic
     * while still working for same-thread usage.
     */
    private static final Deque<String> QUERIES = new ConcurrentLinkedDeque<>();

    @Override
    public String inspect(String sql) {
        QUERIES.add(sql);
        return sql;
    }

    @Override
    @Nullable
    public String getLastSql() {
        return QUERIES.peekLast();
    }

    @Override
    public void clear() {
        QUERIES.clear();
    }
}
