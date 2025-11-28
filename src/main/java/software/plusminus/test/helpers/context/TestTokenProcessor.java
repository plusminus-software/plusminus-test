package software.plusminus.test.helpers.context;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.security.Security;
import software.plusminus.security.service.TokenProcessor;

import java.util.List;
import java.util.Objects;

@ConditionalOnClass(TokenProcessor.class)
@AllArgsConstructor
@Component
public class TestTokenProcessor {

    private List<TokenProcessor> tokenProcessors;

    public String getToken(Security security) {
        return tokenProcessors.stream()
                .map(tokenProcessor -> tokenProcessor.getToken(security))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
