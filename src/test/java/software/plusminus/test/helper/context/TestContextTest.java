package software.plusminus.test.helper.context;

import org.junit.After;
import org.junit.Test;
import software.plusminus.context.Context;

import java.util.Arrays;
import java.util.Collections;

import static software.plusminus.check.Checks.check;

public class TestContextTest {

    private final StringContext stringContext = new StringContext();
    private final IntContext intContext = new IntContext();

    @After
    public void tearDown() {
        new TestContext(Collections.emptyList()).clear();
    }

    @Test
    public void setBindsValueForTheTestThread() {
        TestContext testContext = new TestContext(Collections.singletonList(stringContext));

        testContext.set("hello");

        check(stringContext.get()).is("hello");
    }

    @Test
    public void setPicksContextByValueType() {
        TestContext testContext = new TestContext(Arrays.asList(stringContext, intContext));

        testContext.set(42);
        testContext.set("hello");

        check(intContext.get()).is(42);
        check(stringContext.get()).is("hello");
    }

    @Test
    public void setValueIsVisibleInsideBlocks() {
        TestContext testContext = new TestContext(Collections.singletonList(stringContext));
        testContext.set("hello");

        Context.run(() -> check(stringContext.get()).is("hello"));
    }

    @Test
    public void clearRestoresOwnValues() {
        TestContext testContext = new TestContext(Arrays.asList(stringContext, intContext));
        testContext.set("hello");
        testContext.set(5);

        testContext.clear();

        check(stringContext.get()).is("own");
        check(intContext.get()).is(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowsWhenNoContextMatchesType() {
        TestContext testContext = new TestContext(Collections.emptyList());
        testContext.set("no matching context");
    }

    @Test(expected = IllegalStateException.class)
    public void setThrowsWhenSeveralContextsMatchType() {
        TestContext testContext = new TestContext(Arrays.asList(new StringContext(), new StringContext()));
        testContext.set("ambiguous");
    }

    private static class StringContext implements Context<String> {
        @Override
        public String supply() {
            return "own";
        }
    }

    private static class IntContext implements Context<Integer> {
        @Override
        public Integer supply() {
            return -1;
        }
    }
}
