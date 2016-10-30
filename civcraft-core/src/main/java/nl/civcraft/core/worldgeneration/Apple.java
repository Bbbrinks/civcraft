package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.model.Item;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Apple extends Item {
    private int calories = 200;

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
