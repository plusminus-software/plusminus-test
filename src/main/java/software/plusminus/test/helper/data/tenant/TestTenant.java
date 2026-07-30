package software.plusminus.test.helper.data.tenant;

import software.plusminus.test.helper.TestHelper;

/**
 * Sets the current tenant for the running test.
 *
 */
public interface TestTenant extends TestHelper {

    void use(String tenant);

    void clear();

}
