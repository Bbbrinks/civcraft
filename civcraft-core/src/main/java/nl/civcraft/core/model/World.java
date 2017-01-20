package nl.civcraft.core.model;


import nl.civcraft.core.model.events.StockpileCreated;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class World {


    private final ApplicationEventPublisher publisher;
    private final Set<Stockpile> stockpiles;

    public World(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        stockpiles = new HashSet<>();
    }

    public void addStockpile(Stockpile createdStockpile) {
        this.stockpiles.add(createdStockpile);
        publisher.publishEvent(new StockpileCreated(createdStockpile, this));
    }

    public Optional<Stockpile> getStockPile() {
        return stockpiles.stream().findAny();
    }


}
