package nl.civcraft.core.utils;

import java.util.Random;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class RandomUtil {

    private static final Random randomGenerator = new Random();

    public static int getNextInt(int max) {
        return randomGenerator.nextInt(max);
    }
}
