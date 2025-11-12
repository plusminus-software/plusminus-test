package software.plusminus.test.util;

import lombok.experimental.UtilityClass;
import software.plusminus.context.Context;

@UtilityClass
public class TestContextUtils {

    public <T> void setContextValue(Context<T> context, T value) {
        Context.VALUES.get().put(context, value);
    }
}
