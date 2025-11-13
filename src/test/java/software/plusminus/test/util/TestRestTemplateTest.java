package software.plusminus.test.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import software.plusminus.test.IntegrationTest;
import software.plusminus.test.fixtures.TestEntity;

import static software.plusminus.check.Checks.check;

public class TestRestTemplateTest extends IntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getPage() {
        Page<TestEntity> page = testRestTemplate.getPage(url() + "/page?size=1&page=2", TestEntity.class);
        check(page.getTotalPages()).is(3);
        check(page.getNumber()).is(2);
        check(page.getTotalElements()).is(3);
        check(page.getSize()).is(1);
        check(page.getNumberOfElements()).is(1);
        check(page.getContent().size()).is(1);
        check(page.getContent().get(0).getStringField()).is("first");
        check(page.getContent().get(0).getId()).is(1L);
    }
}
