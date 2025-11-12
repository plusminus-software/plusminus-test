package software.plusminus.test.fixtures;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class TestController {

    @GetMapping("/page")
    public Page<TestEntity> getPage(Pageable pageable) {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        entity.setStringField("first");
        return new PageImpl<>(Collections.singletonList(entity), pageable, 3);
    }
}
