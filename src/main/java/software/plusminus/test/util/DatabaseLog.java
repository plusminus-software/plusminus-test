package software.plusminus.test.util;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

@Component
public class DatabaseLog implements StatementInspector {

    private static final ThreadLocal<List<String>> QUERIES = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        if (QUERIES.get() == null) {
            QUERIES.set(new ArrayList<>());
        }
        QUERIES.get().add(sql);
        return sql;
    }

    @Nullable
    public static String getLastSql() {
        List<String> queries = QUERIES.get();
        if (queries == null || queries.isEmpty()) {
            return null;
        }
        return queries.get(queries.size() - 1);
    }

    public static void clear() {
        List<String> queries = QUERIES.get();
        if (queries == null || queries.isEmpty()) {
            return;
        }
        queries.clear();
    }
}
