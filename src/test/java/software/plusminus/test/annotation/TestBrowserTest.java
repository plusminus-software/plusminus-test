package software.plusminus.test.annotation;

import org.junit.Test;
import software.plusminus.util.AnnotationUtils;

import static software.plusminus.check.Checks.check;

@TestBrowser(headless = true)
public class TestBrowserTest {

    @Test
    public void testBrowserAnnotationIsPresent() {
        TestBrowser testBrowser = AnnotationUtils.findAnnotation(TestBrowser.class, this);
        check(testBrowser).isNotNull();
        check(testBrowser.headless()).isTrue();
    }
}
