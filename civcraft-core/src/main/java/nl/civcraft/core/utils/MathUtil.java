package nl.civcraft.core.utils;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class MathUtil {

    private MathUtil() {

    }

    public static long rnd(int max) {
        return rnd(0, max);
    }

    public static long rnd(int min, int max) {
        return min + (int) Math.floor(Math.random() * max);
    }

    @SuppressWarnings("unused")
    public static float rnd(float max) {
        return rnd(0, max);
    }

    public static float rnd(float min, float max) {
        return  (float)(min + (Math.random() * max));
    }
}



