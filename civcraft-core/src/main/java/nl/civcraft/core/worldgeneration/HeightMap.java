package nl.civcraft.core.worldgeneration;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class HeightMap {
    private final int length;
    private final int width;
    private final long[] map;

    public HeightMap(int width,
                     int length) {
        this.width = width;
        this.length = length;
        this.map = new long[width * length];
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public void setHeight(int x,
                          int z,
                          long peakHeight) {
        map[getByCoords(x, z)] = peakHeight;
    }

    private int getByCoords(int x,
                            int z) {
        return x + (width * z);
    }

    public long getHeight(int x,
                          int z) {
        return map[getByCoords(x, z)];
    }

    public void addToHeight(int x,
                            int z,
                            long amount) {
        map[getByCoords(x, z)] += amount;
    }

    public long findHighestPoint() {
        long big = map[0];
        for (long aMap : map)
            if (aMap > big) {
                big = aMap;

            }
        return big;
    }
}
