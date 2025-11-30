package software.plusminus.test.helpers.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.test.helpers.TestHelper;

import javax.transaction.Transactional;

@ConditionalOnClass(Transactional.class)
@Component
public class TestTransactionHelper implements TestHelper {

    @Transactional
    public void run(Runnable run) {
        run.run();
    }
}
