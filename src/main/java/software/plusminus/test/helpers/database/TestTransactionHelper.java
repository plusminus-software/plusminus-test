package software.plusminus.test.helpers.database;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@ConditionalOnClass(Transactional.class)
@Component
public class TestTransactionHelper {

    @Transactional
    public void run(Runnable run) {
        run.run();
    }
}
