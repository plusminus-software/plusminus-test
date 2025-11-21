package software.plusminus.test.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import software.plusminus.test.helpers.database.JdbcDatabaseManager;
import software.plusminus.test.helpers.database.TestDatabaseManager;

import java.util.Optional;
import javax.sql.DataSource;

@Configuration
@ComponentScan("software.plusminus.test")
public class TestConfiguration implements TestHelper {

    @Bean
    TestDatabaseManager testDatabaseManager(Optional<DataSource> dataSource) {
        //TODO replace with @ConditionalOnBean(DataSource.class). For some reason it does not work.
        return dataSource.map(JdbcDatabaseManager::new)
                .orElse(null);
    }
}
