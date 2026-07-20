package software.plusminus.test.helper.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import javax.sql.DataSource;

@Configuration
public class TestDataConfiguration {

    @Bean
    TestDatabase testDatabaseHelper(Optional<DataSource> dataSource) {
        return dataSource.map(JdbcTestDatabase::new)
                .orElse(null);
    }
}
