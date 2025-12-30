package software.plusminus.test.helpers.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.test.helpers.TestHelper;

import java.util.concurrent.Callable;
import javax.transaction.Transactional;

@ConditionalOnClass(Transactional.class)
@Component
public class TestTransactionHelper implements TestHelper {

    @Transactional
    public void run(Runnable run) {
        run.run();
    }

    @Transactional
    public <T> T call(Callable<T> call) {
        try {
            return call.call();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
