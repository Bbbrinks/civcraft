package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Haul;
import nl.civcraft.core.tasks.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static nl.civcraft.test.util.ThrowableAssertion.assertThrown;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class HaulableTest {
    private Haulable underTest;
    private GameObject testGameObject;
    @Mock
    private ItemComponent itemComponent;
    @Mock
    private AStarPathFinder pathFinder;
    @Mock
    private Stockpile stockpile;
    @Mock
    private VoxelManager voxelManager;

    @Before
    public void setUp() throws Exception {
        testGameObject = new GameObject();
        testGameObject.addComponent(itemComponent);

        underTest = new Haulable();
        underTest.addTo(testGameObject);
    }

    @Test
    public void addTo_noItem() throws Exception {
        testGameObject.removeComponent(itemComponent);
        assertThrown(() -> underTest.addTo(testGameObject)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getTask() throws Exception {
        Task task = underTest.getTask(stockpile, pathFinder, voxelManager);
        assertThat(task, instanceOf(Haul.class));
    }


}