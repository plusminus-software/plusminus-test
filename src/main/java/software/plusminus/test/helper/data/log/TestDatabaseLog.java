package software.plusminus.test.helper.data.log;

import software.plusminus.test.helper.TestHelper;

import javax.annotation.Nullable;

public interface TestDatabaseLog extends TestHelper {

    @Nullable
    String getLastSql();

    void clear();

}
