package software.plusminus.test.helper.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.plusminus.security.service.TokenContext;

/**
 * Security-related test beans, separated from {@link software.plusminus.test.helper.TestConfiguration}
 * so that the latter stays free of references to optional plusminus-security-core classes.
 * This class is discovered through component scanning, where {@code @ConditionalOnClass} is
 * evaluated on ASM metadata: in projects without plusminus-security-core the class is skipped
 * without ever being loaded.
 */
@Configuration
@ConditionalOnClass(TokenContext.class)
public class TestSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(TokenContext.class)
    TestTokenContext testTokenContext() {
        return new TestTokenContext();
    }
}
