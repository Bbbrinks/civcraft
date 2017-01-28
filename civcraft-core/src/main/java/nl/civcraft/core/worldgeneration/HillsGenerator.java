package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.utils.MathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class HillsGenerator implements HeightMapGenerator {
    private static final Logger LOGGER = LogManager.getLogger();

    private final float maxHillHeight;
    private final float minHillRadius;
    private final float maxHillRadius;
    private final int minHills;
    private final int maxHills;

    public HillsGenerator(float maxHillHeight, float minHillRadius, float maxHillRadius, int minHills, int maxHills) {
        this.maxHillHeight = maxHillHeight;
        this.minHillRadius = minHillRadius;
        this.maxHillRadius = maxHillRadius;
        this.minHills = minHills;
        this.maxHills = maxHills;
    }

    public HeightMap generateRandomHeightMap(int width, int length) {
        HeightMap heightMap = new HeightMap(width, length);
        int iterations = MathUtil.rnd(minHills, maxHills);

        for (; iterations > 0; iterations--) {
            LOGGER.trace("Adding random hill to height map. (" + iterations + " hills left to add)");
            addRandomHill(heightMap);
        }
        normalize(maxHillHeight, heightMap);
        return heightMap;
    }

    private void addRandomHill(HeightMap heightMap) {
        int peakX = MathUtil.rnd(heightMap.getLength());
        int peakZ = MathUtil.rnd(heightMap.getWidth());

        float hillRadius = MathUtil.rnd(minHillRadius, maxHillRadius);
        handlePeak(peakX, peakZ, hillRadius, heightMap);
    }

    private void normalize(float maxHillHeight, HeightMap heightMap) {
        long highestPoint = heightMap.findHighestPoint();
        float normalizer = maxHillHeight / highestPoint;
        for (int x = 0; x < heightMap.getWidth(); x++) {
            for (int z = 0; z < heightMap.getLength(); z++) {
                heightMap.setHeight(x, z, Math.round((heightMap.getHeight(x, z)) * normalizer));
            }
        }
    }

    private void handlePeak(int peakX, int peakZ, float hillRadius, HeightMap heightMap) {
        for (int x = 0; x < heightMap.getWidth(); x++) {
            for (int z = 0; z < heightMap.getLength(); z++) {
                long height = Math.round(Math.pow(hillRadius, 2) - (Math.pow((double) x - peakX, 2) + Math.pow((double) z - peakZ, 2)));
                heightMap.addToHeight(x, z, height > 0 ? height : 0);
            }
        }
    }
}
