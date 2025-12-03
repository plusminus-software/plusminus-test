package software.plusminus.test.helpers.data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;

@AllArgsConstructor
@SuppressFBWarnings("SQL_INJECTION_JDBC")
public class JdbcDatabaseHelper implements TestDatabaseHelper {

    private static final String POSTGRESQL = "postgresql";
    private static final String MYSQL = "mysql";
    private static final String H2 = "h2";
    private static final Set<String> SUPPORTED_DBS = new HashSet<>(Arrays.asList(POSTGRESQL, MYSQL, H2));

    private DataSource dataSource;

    @Override
    public void cleanupDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String dbName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            if (!SUPPORTED_DBS.contains(dbName)) {
                throw new IllegalStateException("Unsupported DB: " + dbName);
            }
            disableForeignKeys(connection, dbName);
            List<String> tableNames = getAllTableNames(connection, dbName);
            truncateTables(connection, dbName, tableNames);
            resetSequences(connection, dbName);
            enableForeignKeys(connection, dbName);
            connection.commit();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String updateQueryWithParameters(String originalQuery, String... parameters) {
        long questionMarksCount = originalQuery.chars()
                .filter(c -> c == '?')
                .count();
        if (parameters.length != questionMarksCount) {
            throw new IllegalArgumentException("Cannot update query: the query expected "
                    + questionMarksCount + " parameters where there are " + parameters.length + " provided");
        }
        String updatedQuery = originalQuery;
        for (Object parameter : parameters) {
            updatedQuery = updatedQuery.replaceFirst("\\?", parameter.toString());
        }
        return updatedQuery;
    }

    private List<String> getAllTableNames(Connection connection, String dbName) throws SQLException {
        String query;
        if (dbName.contains(POSTGRESQL)) {
            query = "SELECT table_name FROM information_schema.tables WHERE table_schema='public'";
        } else if (dbName.contains(MYSQL)) {
            query = "SHOW TABLES";
        } else if (dbName.contains(H2)) {
            query = "SHOW TABLES";
        } else {
            throw new IllegalStateException("Unsupported DB: " + dbName);
        }

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            List<String> tableNames = new ArrayList<>();
            while (rs.next()) {
                tableNames.add(rs.getString(1));
            }
            return tableNames;
        }
    }

    private void disableForeignKeys(Connection connection, String dbName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            if (dbName.contains(POSTGRESQL)) {
                stmt.execute("SET session_replication_role = 'replica'");
            } else if (dbName.contains(MYSQL)) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            } else if (dbName.contains(H2)) {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
            }
        }
    }

    private void truncateTables(Connection connection, String dbName, List<String> tableNames) throws SQLException {
        String truncateTable = "TRUNCATE TABLE ";
        try (Statement stmt = connection.createStatement()) {
            for (String table : tableNames) {
                if (!shouldTruncateTable(table)) {
                    continue;
                }
                if (dbName.contains(POSTGRESQL)) {
                    stmt.execute(truncateTable + table + " RESTART IDENTITY CASCADE");
                } else if (dbName.contains(MYSQL)) {
                    stmt.execute(truncateTable + table);
                } else if (dbName.contains(H2)) {
                    stmt.execute(truncateTable + table);
                }
            }
        }
    }

    private void resetSequences(Connection connection, String dbName) throws SQLException {
        if (dbName.contains(H2)) {
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES")) {
                while (rs.next()) {
                    String sequenceName = rs.getString(1);
                    stmt.addBatch("ALTER SEQUENCE " + sequenceName + " RESTART WITH 1");
                }
                stmt.executeBatch();
            }
        }
    }

    private void enableForeignKeys(Connection connection, String dbName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            if (dbName.contains(POSTGRESQL)) {
                stmt.execute("SET session_replication_role = 'origin'");
            } else if (dbName.contains(MYSQL)) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            } else if (dbName.contains(H2)) {
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            }
        }
    }

    private boolean shouldTruncateTable(String tableName) {
        if (tableName.contains("flyway_schema_history")) {
            return false;
        }
        return true;
    }
}
