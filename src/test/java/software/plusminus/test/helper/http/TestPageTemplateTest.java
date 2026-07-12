package software.plusminus.test.helper.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class TestPageTemplateTest {

    @Test(expected = IllegalArgumentException.class)
    public void throwsForNonPageResponseType() {
        TestPageTemplate template = new TestPageTemplate(new ObjectMapper(), null);
        template.getForGenericObject("http://localhost/x", String.class, String.class);
    }
}
