package nl.civcraft.core.npc;

import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civvy extends AbstractGameComponent {

    private TaskManager taskManager;
    private Task task;


    public Civvy() {
    }

    public void subscribe(TaskManager taskManager) {
        this.taskManager = taskManager;
        taskManager.addSubscriber(this);
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void handleTask(Task task) {
        if (!task.getState().equals(Task.State.CONTINUAL)) {
            task.setState(Task.State.DOING);
        }
        this.task = task;
    }

    public void update(float tpf) {
        if (task != null) {
            Task.Result affect = task.affect(this.getGameObject(), tpf);
            if (affect.equals(Task.Result.COMPLETED)) {
                task.completed(this);
            } else if (affect.equals(Task.Result.FAILED)) {
                task.failed(this);
            }
        } else {
            taskManager.requestTask(this);
        }
    }

    public Vector3f getLocationAt(GameObject voxel) {
        return voxel.getTransform().getTranslation().add(new Vector3f(0, 1, 0));
    }

    public static class Factory implements GameComponentFactory<Civvy> {
        @Override
        public Civvy build() {
            return new Civvy();
        }

        @Override
        public Class<Civvy> getComponentType() {
            return Civvy.class;
        }
    }
}
