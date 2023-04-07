package nl.civcraft.test.util;


import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.joml.Vector3f;
import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.argThat;

/**
 * Created by Bob on 17-2-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Vector3fMatcher extends ArgumentMatcher<Vector3f> {

    public static float DEFAULT_RANGE = 0.0001f;

    private final Vector3f value;
    private final float range;

    public Vector3fMatcher(Vector3f value, float range) {
        this.value = value;
        this.range = range;
    }

    @Factory
    public static Vector3f isInRange(Vector3f value) {
        return isInRange(value, DEFAULT_RANGE);
    }

    @Factory
    public static Vector3f isInRange(Vector3f value, float range) {
        return argThat(new Vector3fMatcher(value, range));
    }

    @Override
    public boolean matches(Object item) {
        return item instanceof Vector3f && value.distance((Vector3f) item) < range;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(value);
    }
}
