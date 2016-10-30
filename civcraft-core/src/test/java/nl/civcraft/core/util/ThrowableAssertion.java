package nl.civcraft.core.util;

import org.junit.Assert;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ThrowableAssertion {

    private final Throwable caught;

    public ThrowableAssertion(Throwable caught) {
        this.caught = caught;
    }

    public static ThrowableAssertion assertThrown(ExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.throwException();
        } catch (Throwable caught) {
            return new ThrowableAssertion(caught);
        }
        throw new ExceptionNotThrownAssertionError();
    }

    public ThrowableAssertion isInstanceOf(Class<? extends Throwable> exceptionClass) {
        Assert.assertThat(caught, isA((Class<Throwable>) exceptionClass));
        return this;
    }

    public ThrowableAssertion hasMessage(String expectedMessage) {
        Assert.assertThat(caught.getMessage(), equalTo(expectedMessage));
        return this;
    }

    public ThrowableAssertion hasNoCause() {
        Assert.assertThat(caught.getCause(), nullValue());
        return this;
    }

    public ThrowableAssertion hasCauseInstanceOf(Class<? extends Throwable> exceptionClass) {
        Assert.assertThat(caught.getCause(), notNullValue());
        Assert.assertThat(caught.getCause(), isA((Class<Throwable>) exceptionClass));
        return this;
    }

    @FunctionalInterface
    public interface ExceptionThrower {
        void throwException() throws Throwable;
    }

    public static class ExceptionNotThrownAssertionError extends AssertionError {
        public ExceptionNotThrownAssertionError() {
            super("Expected exception was not thrown.");
        }
    }
}