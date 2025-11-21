package software.plusminus.test.helpers.context;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.context.Context;
import software.plusminus.spring.SpringUtil;
import software.plusminus.test.helpers.TestHelper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@ConditionalOnClass(Context.class)
@Component
public class TestContextManager implements TestHelper {

    private List<Context<?>> contexts;

    public void init() {
        Context.init();
    }

    public void clear() {
        Context.clear();
    }

    public <T> void setValue(T value) {
        List<Context<T>> filteredContexts = contexts.stream()
                .filter(context -> SpringUtil.resolveGenericType(context, Context.class) == value.getClass())
                .map(context -> (Context<T>) context)
                .collect(Collectors.toList());
        if (filteredContexts.size() != 1) {
            throw new IllegalStateException("Should find exactly one context for type " + value.getClass()
                    + " but found " + filteredContexts.size());
        }
        setValue(filteredContexts.get(0), value);
    }

    private <T> void setValue(Context<T> context, T value) {
        Context.VALUES.get().put(context, value);
    }
}
