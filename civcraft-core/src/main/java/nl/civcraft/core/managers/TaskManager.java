package nl.civcraft.core.managers;

import nl.civcraft.core.event.SystemUpdate;
import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.World;
import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.model.events.EntityCreatedEvent;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Task;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class TaskManager {

    private final World world;
    private final AStarPathFinder pathFinder;
    private List<Civvy> civvies;
    private List<Task> tasks;

    @Autowired
    public TaskManager(WorldManager worldManager, AStarPathFinder pathFinder) {
        this.pathFinder = pathFinder;
        civvies = new CopyOnWriteArrayList<>();
        tasks = new ArrayList<>();
        world = worldManager.getWorld();
    }

    @EventListener
    public void update(SystemUpdate systemUpdate) {
        for (Civvy civvy : civvies) {
            civvy.update(systemUpdate.getTpf());
        }
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        civvyCreated.getCivvy().subscribe(this);
    }

    @EventListener
    public void handleEntityCreated(EntityCreatedEvent entityCreatedEvent) {
        GameObject entity = entityCreatedEvent.getEntity();
        Optional<Haulable> component = entity.getComponent(Haulable.class);
        Optional<Stockpile> stockPile = world.getStockPile();
        if (component.isPresent() && stockPile.isPresent()) {
            addTask(component.get().getTask(stockPile.get(), pathFinder));
        }
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void addSubscriber(Civvy civvy) {
        civvies.add(civvy);
    }

    public void requestTask(Civvy civvy) {
        List<Task> todo = tasks.stream().filter(t -> t.getState().equals(Task.State.TODO)).collect(Collectors.toList());
        if (!todo.isEmpty()) {
            civvy.handleTask(todo.get((int) MathUtil.rnd(1, todo.size()) - 1));
            return;
        }
        List<Task> continual = tasks.stream().filter(t -> t.getState().equals(Task.State.CONTINUAL)).collect(Collectors.toList());
        if (!continual.isEmpty()) {
            civvy.handleTask(continual.get((int) MathUtil.rnd(1, continual.size()) - 1));
            return;
        }
    }
}
