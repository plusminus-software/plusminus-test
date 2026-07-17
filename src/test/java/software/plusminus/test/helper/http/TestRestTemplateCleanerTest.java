package software.plusminus.test.helper.http;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static software.plusminus.check.Checks.check;

public class TestRestTemplateCleanerTest {

    private static final String BEAN_NAME = "org.springframework.boot.test.web.client.TestRestTemplate";

    @Test
    public void removesTestRestTemplateDefinitionWhenWebIsAbsent() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registry.registerBeanDefinition(BEAN_NAME, new RootBeanDefinition(Object.class));

        new TestRestTemplateCleaner(false).postProcessBeanDefinitionRegistry(registry);

        check(registry.containsBeanDefinition(BEAN_NAME)).isFalse();
    }

    @Test
    public void keepsTestRestTemplateDefinitionWhenWebIsPresent() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registry.registerBeanDefinition(BEAN_NAME, new RootBeanDefinition(Object.class));

        new TestRestTemplateCleaner(true).postProcessBeanDefinitionRegistry(registry);

        check(registry.containsBeanDefinition(BEAN_NAME)).isTrue();
    }

    @Test
    public void ignoresRegistryWithoutTestRestTemplateDefinition() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();

        new TestRestTemplateCleaner(false).postProcessBeanDefinitionRegistry(registry);

        check(registry.containsBeanDefinition(BEAN_NAME)).isFalse();
    }

    @Test
    public void defaultConstructorDetectsWebOnClasspath() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        registry.registerBeanDefinition(BEAN_NAME, new RootBeanDefinition(Object.class));

        new TestRestTemplateCleaner().postProcessBeanDefinitionRegistry(registry);

        check(registry.containsBeanDefinition(BEAN_NAME)).isTrue();
    }
}
