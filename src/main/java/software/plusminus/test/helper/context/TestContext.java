package software.plusminus.test.helper.context;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import software.plusminus.context.Context;
import software.plusminus.spring.SpringUtil;
import software.plusminus.test.helper.TestHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@ConditionalOnClass(Context.class)
@Component
public class TestContext implements TestHelper {

    private List<Context<?>> contexts;

    @SuppressWarnings("unchecked")
    public void clear() {
        ((ThreadLocal<Object>) readField(scopeClass(), "CURRENT", null)).remove();
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

    @SuppressWarnings("unchecked")
    @SuppressFBWarnings(value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Static members are accessed with a null target")
    private <T> void set(Context<T> context, T value) {
        Object scope = ((ThreadLocal<Object>) readField(scopeClass(), "CURRENT", null)).get();
        if (scope == null) {
            Method open = Objects.requireNonNull(ReflectionUtils.findMethod(scopeClass(), "open", Map.class));
            ReflectionUtils.makeAccessible(open);
            scope = ReflectionUtils.invokeMethod(open, null, new HashMap<>());
        }
        ((Map<Context<?>, Object>) readField(scopeClass(), "bound", scope)).put(context, value);
    }

    private static Class<?> scopeClass() {
        try {
            return Class.forName("software.plusminus.context.ThreadLocalScope", true,
                    Context.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object readField(Class<?> type, String name, Object target) {
        Field field = ReflectionUtils.findField(type, name);
        if (field == null) {
            throw new IllegalStateException("No field '" + name + "' on " + type);
        }
        ReflectionUtils.makeAccessible(field);
        Object value = ReflectionUtils.getField(field, target);
        if (value == null) {
            throw new IllegalStateException("Field '" + name + "' is null on " + type);
        }
        return value;
    }
}
