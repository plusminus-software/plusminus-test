package software.plusminus.test.helper.data;

import software.plusminus.test.helper.TestHelper;

public interface TestDatabase extends TestHelper {

    void clear();

    String updateQueryWithParameters(String originalQuery, String... parameters);

}
