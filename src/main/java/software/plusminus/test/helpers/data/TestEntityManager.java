package software.plusminus.test.helpers.data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.test.helpers.TestHelper;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@AllArgsConstructor
@SuppressFBWarnings("SQL_INJECTION_JPA")
@Transactional
@ConditionalOnClass(EntityManager.class)
@Component
public class TestEntityManager implements TestHelper {

    @Delegate
    private EntityManager entityManager;

}
