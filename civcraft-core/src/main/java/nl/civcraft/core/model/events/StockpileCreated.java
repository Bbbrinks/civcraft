package nl.civcraft.core.model.events;

import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.World;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StockpileCreated extends ApplicationEvent {
    private final Stockpile stockpile;

    public StockpileCreated(Stockpile createdStockpile, World world) {
        super(world);
        this.stockpile = createdStockpile;
    }

    public Stockpile getStockpile() {
        return stockpile;
    }
}
