package software.plusminus.test.helper.data;

import org.junit.Test;

import static software.plusminus.check.Checks.check;

public class JdbcTestDatabaseTest {

    private final JdbcTestDatabase database = new JdbcTestDatabase(null);

    @Test
    public void replacesQuestionMarksInOrder() {
        String result = database.updateQueryWithParameters(
                "select * from t where a = ? and b = ?", "1", "2");
        check(result).is("select * from t where a = 1 and b = 2");
    }

    @Test
    public void treatsDollarAndBackslashInParameterLiterally() {
        String result = database.updateQueryWithParameters(
                "select * from t where a = ?", "$1\\x");
        check(result).is("select * from t where a = $1\\x");
    }
}
