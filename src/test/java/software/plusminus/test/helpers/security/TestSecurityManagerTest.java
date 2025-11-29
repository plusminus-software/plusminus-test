package software.plusminus.test.helpers.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.jwt.service.JwtParser;
import software.plusminus.security.Security;
import software.plusminus.test.IntegrationTest;

import static software.plusminus.check.Checks.check;

class TestSecurityManagerTest extends IntegrationTest {

    @Autowired
    private JwtParser jwtParser;

    @Test
    void generateToken() {
        String token = security().generateToken(Security.builder()
                .username("test-user")
                .build());
        Security parsed = jwtParser.parseToken(token);
        check(parsed.getUsername()).is("test-user");
        check(parsed.getOthers().containsKey("iss")).isFalse();
    }

    @Test
    void generateTokenWithIssuer() {
        String token = security().generateToken(Security.builder()
                .username("test-user")
                .build(), "test-host");
        Security parsed = jwtParser.parseToken(token);
        check(parsed.getUsername()).is("test-user");
        check(parsed.getOthers().get("iss")).is("test-host");
    }
}
