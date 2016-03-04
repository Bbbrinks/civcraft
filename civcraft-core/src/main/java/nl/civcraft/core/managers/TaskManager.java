package nl.civcraft.core.managers;

import com.jme3.app.state.AbstractAppState;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.Task;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class TaskManager extends AbstractAppState {

    private List<Civvy> civvies;
    private List<Task> tasks;

    public TaskManager() {
        civvies = new CopyOnWriteArrayList<>();
        tasks = new ArrayList<>();
    }

    @Override
    public void update(float tpf) {
        for (Civvy civvy : civvies) {
            civvy.update(tpf);
        }
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        civvyCreated.getCivvy().subscribe(this);
    }

    public void addSubscriber(Civvy civvy) {
        civvies.add(civvy);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void requestTask(Civvy civvy) {
        List<Task> todo = tasks.stream().filter(t -> t.getState().equals(Task.State.TODO) || t.getState().equals(Task.State.CONTINUAL)).collect(Collectors.toList());
        if (!todo.isEmpty()) {
            civvy.handleTask(todo.get(0));
        }
    }
}
