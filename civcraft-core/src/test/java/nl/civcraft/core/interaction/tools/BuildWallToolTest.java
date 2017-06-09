package nl.civcraft.core.interaction.tools;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.tasks.PlaceBlock;
import nl.civcraft.core.tasks.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildWallToolTest {
    private BuildWallTool underTest;
    @Mock
    private CurrentVoxelHighlighter currentVoxelHighlighter;
    @Mock
    private TaskManager taskManager;
    @Mock
    private PrefabManager stockpileManager;
    @Mock
    private PrefabManager blockManager;

    @Captor
    private ArgumentCaptor<Task> taskCaptor;
    @Mock
    private PrefabManager planningGhostManager;


    @Before
    public void setUp() throws Exception {
        underTest = new BuildWallTool(currentVoxelHighlighter, taskManager, stockpileManager, blockManager, planningGhostManager);
    }

    @Test
    public void testPlacement_startNextToEnd() {
        GameObject start = new GameObject(new Transform(new Vector3f(1, 1, 1)));
        GameObject end = new GameObject(new Transform(new Vector3f(2, 1, 1)));
        when(currentVoxelHighlighter.getCurrentVoxel()).thenReturn(start, end);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(1, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        verify(taskManager, times(2)).addTask(taskCaptor.capture());
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(0)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 1))));
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(1)).getLocationToPlace(), is(new Transform(new Vector3f(2, 2, 1))));
    }


    @Test
    public void testPlacement_endNextToStart() {
        GameObject start = new GameObject(new Transform(new Vector3f(2, 1, 1)));
        GameObject end = new GameObject(new Transform(new Vector3f(1, 1, 1)));
        when(currentVoxelHighlighter.getCurrentVoxel()).thenReturn(start, end);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(1, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        verify(taskManager, times(2)).addTask(taskCaptor.capture());
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(0)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 1))));
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(1)).getLocationToPlace(), is(new Transform(new Vector3f(2, 2, 1))));
    }

    @Test
    public void testPlacement_startBehindEnd() {
        GameObject start = new GameObject(new Transform(new Vector3f(1, 1, 1)));
        GameObject end = new GameObject(new Transform(new Vector3f(1, 1, 2)));
        when(currentVoxelHighlighter.getCurrentVoxel()).thenReturn(start, end);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(1, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        verify(taskManager, times(2)).addTask(taskCaptor.capture());
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(0)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 1))));
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(1)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 2))));
    }


    @Test
    public void testPlacement_endBehindStart() {
        GameObject start = new GameObject(new Transform(new Vector3f(1, 1, 2)));
        GameObject end = new GameObject(new Transform(new Vector3f(1, 1, 1)));
        when(currentVoxelHighlighter.getCurrentVoxel()).thenReturn(start, end);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(1, 0);
        underTest.handleLeftClick(true);
        underTest.handleMouseMotion(0, 0);
        underTest.handleLeftClick(true);
        verify(taskManager, times(2)).addTask(taskCaptor.capture());
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(0)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 1))));
        assertThat(((PlaceBlock) taskCaptor.getAllValues().get(1)).getLocationToPlace(), is(new Transform(new Vector3f(1, 2, 2))));
    }

}