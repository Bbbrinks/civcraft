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

    public HeightMap(int width, int length) {
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

    public void setHeight(int x, int z, long peakHeight) {
        map[getByCoords(x, z)] = peakHeight;
    }

    public long getHeight(int x, int z) {
        return map[getByCoords(x, z)];
    }

    public void addToHeight(int x, int z, long amount) {
        map[getByCoords(x, z)] += amount;
    }

    private int getByCoords(int x, int z) {
        return x + (width * z);
    }

    public long findLowestPoint() {
        long small = map[0];
        for (int i = 0; i < map.length; i++)
            if (map[i] < small) {
                small = map[i];

            }
        return small;
    }

    public long findHighestPoint() {
        long big = map[0];
        for (int i = 0; i < map.length; i++)
            if (map[i] > big) {
                big = map[i];

            }
        return big;
    }
}
