package nl.civcraft.core.managers;

import nl.civcraft.core.event.SystemUpdate;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.Task;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class TaskManager implements Serializable {


    private final List<Civvy> civvies;
    private final List<Task> tasks;

    @Autowired
    public TaskManager(List<Task> defaultTasks) {
        civvies = new CopyOnWriteArrayList<>();
        tasks = new ArrayList<>();
        for (Task defaultTask : defaultTasks) {
            if (defaultTask.getState().equals(Task.State.CONTINUAL)) {
                tasks.add(defaultTask);
            }
        }
    }

    @EventListener
    public void update(SystemUpdate systemUpdate) {
        for (Civvy civvy : civvies) {
            civvy.update(systemUpdate.getTpf());
        }
    }

    @EventListener
    public void addCivvy(GameObjectCreatedEvent civvyCreated) {
        Optional<Civvy> component = civvyCreated.getGameObject().getComponent(Civvy.class);
        if (!component.isPresent()) {
            return;
        }
        component.get().subscribe(this);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void addSubscriber(Civvy civvy) {
        civvies.add(civvy);
    }

    public void requestTask(Civvy civvy) {
        List<Task> todo = tasks.stream().filter(t -> t.getState().equals(Task.State.TODO) && t.canBeHandledBy(civvy)).collect(Collectors.toList());
        if (!todo.isEmpty()) {
            civvy.handleTask(todo.get(MathUtil.rnd(1, todo.size()) - 1));
            return;
        }
        List<Task> continual = tasks.stream().filter(t -> t.getState().equals(Task.State.CONTINUAL)).collect(Collectors.toList());
        if (!continual.isEmpty()) {
            civvy.handleTask(continual.get(MathUtil.rnd(1, continual.size()) - 1));
        }
    }
}
