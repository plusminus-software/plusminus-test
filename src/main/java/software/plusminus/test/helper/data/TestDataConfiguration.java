package software.plusminus.test.helper.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TestDataConfiguration {

    @Bean
    @ConditionalOnBean(DataSource.class)
    TestDatabase testDatabaseHelper(DataSource dataSource) {
        return new JdbcTestDatabase(dataSource);
    }
}
