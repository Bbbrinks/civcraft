package nl.civcraft.test.util;

import com.jme3.math.Vector3f;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * Created by Bob on 17-2-2017.
 * <p>
 * This is probably not worth documenting
 */
public class IsVector3f extends BaseMatcher<Vector3f> {


    private final Vector3f value;
    private final float range;

    public IsVector3f(Vector3f value, float range) {
        this.value = value;
        this.range = range;
    }

    @Factory
    public static Matcher<Vector3f> isInRange(Vector3f value, float range) {
        return new IsVector3f(value, range);
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
