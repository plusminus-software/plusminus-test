package software.plusminus.test.helpers.data;

import software.plusminus.test.helpers.TestHelper;

import javax.annotation.Nullable;

public interface TestDatabaseLog extends TestHelper {

    @Nullable
    String getLastSql();

    void clear();

}
