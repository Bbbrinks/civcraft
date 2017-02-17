package nl.civcraft.core.utils;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class MathUtil {

    private MathUtil() {

    }

    public static int rnd(int max) {
        return rnd(0, max);
    }

    public static int rnd(int min, int max) {
        return min + RandomUtil.getNextInt(max);
    }

    @SuppressWarnings("unused")
    public static float rnd(float max) {
        return rnd(0, max);
    }

    public static float rnd(float min, float max) {
        return  (float)(min + (Math.random() * max));
    }

    public static float valueOrMin(float value, @SuppressWarnings("SameParameterValue") float min) {
        return value > min ? value : min;
    }

    public static boolean between(int min, int value, int max) {
        return min < value && max > value;
    }
}



