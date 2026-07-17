package software.plusminus.test.helper.http;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * Spring Boot registers a TestRestTemplate bean definition for every test with an embedded
 * webEnvironment (e.g. RANDOM_PORT) based on the annotation alone, even when spring-web is not
 * on the classpath and the context is created as a non-web one. Reflecting over that definition
 * then fails with NoClassDefFoundError in non-web consumers, so the definition must be removed
 * before bean factory post-processing validates it.
 */
@Component
public class TestRestTemplateCleaner implements BeanDefinitionRegistryPostProcessor {

    private static final String TEST_REST_TEMPLATE_BEAN = "org.springframework.boot.test.web.client.TestRestTemplate";
    private static final String REST_TEMPLATE_CLASS = "org.springframework.web.client.RestTemplate";

    private final boolean webPresent;

    @Autowired
    public TestRestTemplateCleaner() {
        this(ClassUtils.isPresent(REST_TEMPLATE_CLASS, TestRestTemplateCleaner.class.getClassLoader()));
    }

    TestRestTemplateCleaner(boolean webPresent) {
        this.webPresent = webPresent;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!webPresent && registry.containsBeanDefinition(TEST_REST_TEMPLATE_BEAN)) {
            registry.removeBeanDefinition(TEST_REST_TEMPLATE_BEAN);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing to do: the cleanup happens on the bean definition registry
    }
}
