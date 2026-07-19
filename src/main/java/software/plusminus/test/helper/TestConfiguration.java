package software.plusminus.test.helper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import software.plusminus.security.service.TokenContext;
import software.plusminus.test.helper.data.JdbcTestDatabase;
import software.plusminus.test.helper.data.TestDatabase;
import software.plusminus.test.helper.security.TestTokenContext;

import java.util.Optional;
import javax.sql.DataSource;

@Configuration
@ComponentScan("software.plusminus.test")
public class TestConfiguration implements TestHelper {

    @Bean
    TestDatabase testDatabaseHelper(Optional<DataSource> dataSource) {
        return dataSource.map(JdbcTestDatabase::new)
                .orElse(null);
    }

    @Bean
    @ConditionalOnMissingBean(TokenContext.class)
    TestTokenContext testTokenContext() {
        return new TestTokenContext();
    }
}
