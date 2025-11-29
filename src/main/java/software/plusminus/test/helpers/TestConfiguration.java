package software.plusminus.test.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import software.plusminus.test.helpers.database.JdbcDatabaseHelper;
import software.plusminus.test.helpers.database.TestDatabaseHelper;

import java.util.Optional;
import javax.sql.DataSource;

@Configuration
@ComponentScan("software.plusminus.test")
public class TestConfiguration implements TestHelper {

    @Bean
    TestDatabaseHelper testDatabaseHelper(Optional<DataSource> dataSource) {
        //TODO replace with @ConditionalOnBean(DataSource.class). For some reason it does not work.
        return dataSource.map(JdbcDatabaseHelper::new)
                .orElse(null);
    }
}
