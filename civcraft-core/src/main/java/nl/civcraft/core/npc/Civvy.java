package nl.civcraft.core.npc;

import com.jme3.math.Vector3f;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civvy {
    private final String type;
    private final Npc npc;
    private final float speed;
    private Vector3f location;
    private World world;
    private TaskManager taskManager;
    private Task task;
    private Voxel currentVoxel;

    public Civvy(float x, float y, float z, String type, Npc npc) {
        location = new Vector3f(x, y, z);
        this.type = type;
        this.npc = npc;
        speed = 1.0f;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Npc cloneNpc() {
        return (Npc) npc.clone();
    }

    public float getX() {
        return location.getX();
    }

    public float getY() {
        return location.getY();
    }

    public float getZ() {
        return location.getZ();
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
            boolean affect = task.affect(this, tpf);
            if (affect) {
                task.completed(this);
            }
        } else {
            taskManager.requestTask(this);
        }
    }

    public Voxel currentVoxel() {
        return currentVoxel;
    }

    public void moveToward(Voxel target, float tpf) {
        Vector3f direction = target.getLocation().add(0, 1, 0).subtract(location).normalize();
        location = location.add(direction.mult(tpf * speed));
    }

    public float distance(Voxel target) {
        return target.getLocation().distance(getLocation().subtract(0, 1, 0));
    }

    public Vector3f getLocation() {
        return location;
    }

    public Voxel getCurrentVoxel() {
        return currentVoxel;
    }

    public void setCurrentVoxel(Voxel currentVoxel) {
        this.currentVoxel = currentVoxel;
    }
}
