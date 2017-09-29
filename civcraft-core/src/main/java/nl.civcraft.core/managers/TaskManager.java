package nl.civcraft.core.managers;

import nl.civcraft.core.SystemEventPublisher;
import nl.civcraft.core.gamecomponents.Civvy;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.tasks.Task;
import nl.civcraft.core.utils.MathUtil;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TaskManager {


    private final List<Civvy> civvies;
    private final List<Task> tasks;


    @Inject
    public TaskManager(Set<Task> defaultTasks,
                       SystemEventPublisher systemEventPublisher,
                       @Named("civvy") PrefabManager prefabManager) {
        systemEventPublisher.getPublisher().subscribe(this::update);
        prefabManager.getGameObjectCreated().subscribe(this::addCivvy);
        civvies = new CopyOnWriteArrayList<>();
        tasks = new ArrayList<>();
        for (Task defaultTask : defaultTasks) {
            if (defaultTask.getState().equals(Task.State.CONTINUAL)) {
                tasks.add(defaultTask);
            }
        }
    }

    public void update(float tpf) {
        for (Civvy civvy : civvies) {
            civvy.update(tpf);
        }
    }

    public void addCivvy(GameObject civvyCreated) {
        Civvy component = civvyCreated.getComponent(Civvy.class).orElseThrow(() -> new IllegalStateException("Civvy component not registered in Civvy prefabManager"));
        component.subscribe(this);
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
            civvy.handleTask(todo.get(MathUtil.rnd(1, todo.size()) - 1));
            return;
        }
        List<Task> continual = tasks.stream().filter(t -> t.getState().equals(Task.State.CONTINUAL)).collect(Collectors.toList());
        if (!continual.isEmpty()) {
            civvy.handleTask(continual.get(MathUtil.rnd(1, continual.size()) - 1));
        }
    }
}
