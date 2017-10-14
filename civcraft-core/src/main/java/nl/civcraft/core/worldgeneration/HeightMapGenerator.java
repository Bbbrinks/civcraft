package nl.civcraft.core.worldgeneration;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
@FunctionalInterface
public interface HeightMapGenerator {

    HeightMap generateRandomHeightMap(int width,
                                      int length);
}
