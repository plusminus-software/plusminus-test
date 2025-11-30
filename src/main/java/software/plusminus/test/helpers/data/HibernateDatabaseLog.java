package software.plusminus.test.helpers.data;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

@ConditionalOnClass(StatementInspector.class)
@Component
public class HibernateDatabaseLog implements TestDatabaseLog, StatementInspector {

    private static final ThreadLocal<List<String>> QUERIES = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        if (QUERIES.get() == null) {
            QUERIES.set(new ArrayList<>());
        }
        QUERIES.get().add(sql);
        return sql;
    }

    @Override
    @Nullable
    public String getLastSql() {
        List<String> queries = QUERIES.get();
        if (queries == null || queries.isEmpty()) {
            return null;
        }
        return queries.get(queries.size() - 1);
    }

    @Override
    public void clear() {
        QUERIES.remove();
    }
}
