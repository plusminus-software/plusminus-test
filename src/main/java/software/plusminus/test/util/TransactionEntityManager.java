package software.plusminus.test.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@AllArgsConstructor
@Component
@Transactional
@SuppressFBWarnings("SQL_INJECTION_JPA")
public class TransactionEntityManager {

    @Delegate
    private EntityManager entityManager;

}
