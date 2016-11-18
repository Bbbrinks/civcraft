package nl.civcraft.core.npc;

import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civvy extends AbstractGameComponent {

    private World world;
    private TaskManager taskManager;
    private Task task;


    public Civvy() {
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
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

    public Vector3f getLocationAtVoxel(Voxel voxel) {
        return voxel.getLocation().add(new Vector3f(0, 1, 0));
    }

    public static class Factory implements GameComponentFactory<Civvy> {
        @Override
        public GameComponent build() {
            return new Civvy();
        }

        @Override
        public Class<Civvy> getComponentType() {
            return Civvy.class;
        }
    }
}
