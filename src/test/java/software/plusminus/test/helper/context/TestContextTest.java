package software.plusminus.test.helper.context;

import org.junit.Test;
import software.plusminus.context.ConstantContext;
import software.plusminus.context.Context;
import software.plusminus.context.ThreadLocalContext;
import software.plusminus.context.WritableContext;

import java.util.Arrays;
import java.util.Collections;

import static software.plusminus.check.Checks.check;

public class TestContextTest {

    @Test
    public void setWritableContext() {
        StringWritableContext context = new StringWritableContext();
        TestContext testContext = new TestContext(Collections.singletonList(context));

        testContext.set("hello");

        check(context.get()).is("hello");
    }

    @Test
    public void setThreadLocalContext() {
        IntThreadLocalContext context = new IntThreadLocalContext();
        TestContext testContext = new TestContext(Collections.singletonList(context));

        testContext.set(42);

        check(context.get()).is(42);
    }

    @Test
    public void setConstantContext() {
        LongConstantContext context = new LongConstantContext();
        TestContext testContext = new TestContext(Collections.singletonList(context));

        testContext.set(7L);

        check(context.get()).is(7L);
    }

    @Test
    public void clearOnlyAffectsClearableContexts() {
        IntThreadLocalContext clearable = new IntThreadLocalContext();
        StringWritableContext notClearable = new StringWritableContext();
        TestContext testContext = new TestContext(Arrays.asList(clearable, notClearable));
        testContext.set(5);
        notClearable.set("keep");

        testContext.clear();

        check(clearable.get()).isNull();
        check(notClearable.get()).is("keep");
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowsWhenNoContextMatchesType() {
        TestContext testContext = new TestContext(Collections.emptyList());
        testContext.set("no matching context");
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowsForUnsupportedContextType() {
        TestContext testContext = new TestContext(Collections.singletonList(new CharContext()));
        testContext.set('a');
    }

    private static class StringWritableContext implements WritableContext<String> {
        private String value;

        @Override
        public String get() {
            return value;
        }

        @Override
        public void set(String newValue) {
            this.value = newValue;
        }

        @Override
        public void replace(String newValue) {
            this.value = newValue;
        }
    }

    private static class IntThreadLocalContext extends ThreadLocalContext<Integer> {
        IntThreadLocalContext() {
            super(new ThreadLocal<>());
        }
    }

    private static class LongConstantContext extends ConstantContext<Long> {
        LongConstantContext() {
            super(0L);
        }
    }

    private static class CharContext implements Context<Character> {
        @Override
        public Character get() {
            return 'x';
        }
    }
}
