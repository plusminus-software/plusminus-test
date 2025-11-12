package software.plusminus.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static software.plusminus.check.Checks.check;

@ConditionalOnClass(org.springframework.boot.test.web.client.TestRestTemplate.class)
@Component
public class TestRestTemplate {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private org.springframework.boot.test.web.client.TestRestTemplate restTemplate;

    @SuppressWarnings("PMD.LooseCoupling")
    public <T> Page<T> getPage(String url, Class<T> type) {
        ResponseEntity<? extends Map> response = restTemplate.getForEntity(url, LinkedHashMap.class);
        check(response.getStatusCode()).is(HttpStatus.OK);
        Map<?, ?> body = response.getBody();
        if (body == null) {
            return null;
        }
        List<T> content = ((List<Object>) body.get("content")).stream()
                .map(e -> objectMapper.convertValue(e, type))
                .collect(Collectors.toList());
        return new PageImpl<>(
                content,
                PageRequest.of((Integer) body.get("number"), (Integer) body.get("size")),
                (Integer) body.get("totalElements")
        );
    }
}
