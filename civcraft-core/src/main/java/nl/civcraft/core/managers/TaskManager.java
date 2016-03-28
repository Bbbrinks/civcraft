package nl.civcraft.core.managers;

import com.jme3.app.state.AbstractAppState;
import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.Task;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
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
