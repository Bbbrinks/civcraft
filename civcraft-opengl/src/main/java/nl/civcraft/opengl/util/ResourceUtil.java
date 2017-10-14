package nl.civcraft.opengl.util;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
public class ResourceUtil {

    public static String readResource(String name) throws IOException {
        return new Scanner(ResourceUtil.class.getClassLoader().getResourceAsStream(name), "UTF-8").useDelimiter("\\A").next();

    }
}
