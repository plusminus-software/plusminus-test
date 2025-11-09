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

    public static String explainLastSql(Object... parameters) {
        String lastSql = DatabaseLog.getLastSql();
        if (lastSql == null) {
            throw new IllegalStateException("There is no queries to explain");
        }
        long questionCount = lastSql.chars()
                .filter(c -> c == '?')
                .count();
        if (parameters.length != questionCount) {
            throw new IllegalArgumentException("Cannot explain last sql: the query expected "
                    + questionCount + " parameters where there are " + parameters.length + " provided");
        }
        String explainSql = "EXPLAIN " + lastSql;
        for (Object parameter : parameters) {
            explainSql = explainSql.replaceFirst("\\?", parameter.toString());
        }
        return explainSql;
    }
}
