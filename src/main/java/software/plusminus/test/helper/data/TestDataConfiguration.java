package software.plusminus.test.helper.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
public class TestDataConfiguration {

    @Bean
    TestDatabase testDatabaseHelper(Optional<DataSource> dataSource) {
        return dataSource.map(JdbcTestDatabase::new)
                .orElse(null);
    }
}
