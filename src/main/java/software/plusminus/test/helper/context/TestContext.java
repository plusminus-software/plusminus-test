package software.plusminus.test.helper.context;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import software.plusminus.context.ClearableContext;
import software.plusminus.context.ConstantContext;
import software.plusminus.context.Context;
import software.plusminus.context.ThreadLocalContext;
import software.plusminus.context.WritableContext;
import software.plusminus.spring.SpringUtil;
import software.plusminus.test.helper.TestHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@ConditionalOnClass(Context.class)
@Component
public class TestContext implements TestHelper {

    private List<Context<?>> contexts;

    public void clear() {
        contexts.stream()
                .filter(ClearableContext.class::isInstance)
                .forEach(context -> ((ClearableContext<?>) context).clear());
    }

    public <T> void set(T value) {
        List<Context<T>> filteredContexts = contexts.stream()
                .filter(context -> SpringUtil.resolveGenericType(context, Context.class) == value.getClass())
                .map(context -> (Context<T>) context)
                .collect(Collectors.toList());
        if (filteredContexts.size() != 1) {
            throw new IllegalStateException("Should find exactly one context for type " + value.getClass()
                    + " but found " + filteredContexts.size());
        }
        set(filteredContexts.get(0), value);
    }

    private <T> void set(Context<T> context, T value) {
        if (context instanceof WritableContext) {
            ((WritableContext<T>) context).set(value);
        } else if (context instanceof ThreadLocalContext) {
            setThreadLocalValue((ThreadLocalContext<T>) context, value);
        } else if (context instanceof ConstantContext) {
            setPrivateField(context, "value", value);
        } else {
            throw new IllegalStateException("Unsupported context type: " + context.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void setThreadLocalValue(ThreadLocalContext<T> context, T value) {
        ThreadLocal<T> threadLocal = (ThreadLocal<T>) readPrivateField(context, "threadLocal");
        threadLocal.set(value);
    }

    private Object readPrivateField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field '" + fieldName + "' on " + target.getClass(), e);
        }
    }

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot set field '" + fieldName + "' on " + target.getClass(), e);
        }
    }
}
