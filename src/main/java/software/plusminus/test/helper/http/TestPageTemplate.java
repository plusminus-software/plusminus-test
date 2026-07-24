package software.plusminus.test.helper.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import software.plusminus.test.helper.TestHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnClass(Page.class)
@Component
public class TestPageTemplate implements TestHelper {

    private ObjectMapper objectMapper;
    private TestRestTemplate testRestTemplate;

    @SuppressWarnings("PMD.LooseCoupling")
    public <T, G> T getForGenericObject(String url,
                                        Class<T> responseType,
                                        Class<G> genericType,
                                        Object... urlVariables) {
        if (responseType == Page.class) {
            Map<?, ?> responseBody = testRestTemplate.getForObject(url, LinkedHashMap.class, urlVariables);
            List<G> content = ((List<Object>) responseBody.get("content")).stream()
                    .map(e -> objectMapper.convertValue(e, genericType))
                    .collect(Collectors.toList());
            return (T) new PageImpl<>(
                    content,
                    PageRequest.of(((Number) responseBody.get("number")).intValue(),
                            ((Number) responseBody.get("size")).intValue()),
                    ((Number) responseBody.get("totalElements")).longValue()
            );
        }
        throw new IllegalArgumentException("Unknown response type " + responseType);
    }
}
