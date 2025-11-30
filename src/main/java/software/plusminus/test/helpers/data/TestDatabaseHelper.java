package software.plusminus.test.helpers.data;

import software.plusminus.test.helpers.TestHelper;

public interface TestDatabaseHelper extends TestHelper {

    void cleanupDatabase();

    String updateQueryWithParameters(String originalQuery, String... parameters);

}
