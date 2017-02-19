package nl.civcraft.core.pathfinding;

import com.jme3.math.Vector3f;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Queue;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static nl.civcraft.test.util.VoxelUtil.createVoxel;
import static nl.civcraft.test.util.VoxelUtil.createVoxels;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Bob on 19-2-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class AStarPathFinderTest {

    private AStarPathFinder underTest;
    @Mock
    private GameObject testCivvy;
    @Mock
    private PathFindingTarget target;
    private VoxelManager voxelManager;

    @Before
    public void setUp() throws Exception {
        voxelManager = new VoxelManager();
        underTest = new AStarPathFinder();

        when(target.getMaxSearchArea(any())).thenReturn(500);
    }

    @Test
    public void testFindPath_targetEqualsStart() {
        GameObject start = new GameObject();
        when(target.isReached(eq(testCivvy), aStartNodeOf(start))).thenReturn(true);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, target);
        assertThat(path, optionalWithValue(hasSize(1)));
    }

    private static AStarNode aStartNodeOf(GameObject start) {
        return argThat(new ArgumentMatcher<AStarNode>() {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof AStarNode && start.equals(((AStarNode) argument).getGameObject());
            }
        });
    }

    @Test
    public void testFindPath_directNeighbour() {
        GameObject start = createVoxel(Vector3f.ZERO, voxelManager);
        GameObject targetVoxel = createVoxel(Vector3f.UNIT_X, voxelManager);
        target = new MoveToVoxelTarget(targetVoxel);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, target);
        assertThat(path, optionalWithValue(hasSize(2)));
        assertThat(path.get().poll(), is(start));
        assertThat(path.get().poll(), is(targetVoxel));
    }

    @Test
    public void testFindPath_flat3x3WithHole() {
        createVoxels(new boolean[][][]
                {
                        {
                                {true, true, true},
                                {true, false, false},
                                {true, true, true}
                        }
                }, voxelManager);
        GameObject start = voxelManager.getVoxelAt(0, 0, 0).get();
        GameObject targetVoxel = voxelManager.getVoxelAt(2, 0, 2).get();
        this.target = new MoveToVoxelTarget(targetVoxel);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, this.target);
        assertThat(path, optionalWithValue(hasSize(5)));
        assertThat(path.get().poll(), is(start));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(0, 0, 1).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(0, 0, 2).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(1, 0, 2).get()));
        assertThat(path.get().poll(), is(targetVoxel));
    }

    @Test
    public void testFindPath_3x3WithPartialWall() {
        createVoxels(new boolean[][][]
                {
                        {
                                {true, true, true},
                                {true, true, true},
                                {true, true, true}
                        },
                        {
                                {false, false, false},
                                {true, true, false},
                                {false, false, false}
                        },
                        {
                                {false, false, false},
                                {true, true, false},
                                {false, false, false}
                        }
                }, voxelManager);
        GameObject start = voxelManager.getVoxelAt(0, 0, 0).get();
        GameObject targetVoxel = voxelManager.getVoxelAt(2, 0, 2).get();
        this.target = new MoveToVoxelTarget(targetVoxel);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, this.target);
        assertThat(path, optionalWithValue(hasSize(5)));
        assertThat(path.get().poll(), is(start));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(1, 0, 0).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(2, 0, 0).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(2, 0, 1).get()));
        assertThat(path.get().poll(), is(targetVoxel));
    }

    @Test
    public void testFindPath_3x3WithFullWall() {
        createVoxels(new boolean[][][]
                {
                        {
                                {true, true, true},
                                {true, true, true},
                                {true, true, true}
                        },
                        {
                                {false, false, false},
                                {true, true, true},
                                {false, false, false}
                        },
                        {
                                {false, false, false},
                                {true, true, true},
                                {false, false, false}
                        }
                }, voxelManager);
        GameObject start = voxelManager.getVoxelAt(0, 0, 0).get();
        GameObject targetVoxel = voxelManager.getVoxelAt(2, 0, 2).get();
        this.target = new MoveToVoxelTarget(targetVoxel);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, this.target);
        assertThat(path, emptyOptional());
    }

    @Test
    public void testFindPath_3x3WithHill() {
        createVoxels(new boolean[][][]
                {
                        {
                                {true, true, true},
                                {true, true, true},
                                {true, true, true}
                        },
                        {
                                {false, false, false},
                                {true, true, true},
                                {false, false, false}
                        }
                }, voxelManager);
        GameObject start = voxelManager.getVoxelAt(0, 0, 0).get();
        GameObject targetVoxel = voxelManager.getVoxelAt(2, 0, 2).get();
        this.target = new MoveToVoxelTarget(targetVoxel);
        Optional<Queue<GameObject>> path = underTest.findPath(testCivvy, start, this.target);
        assertThat(path, optionalWithValue(hasSize(5)));
        assertThat(path.get().poll(), is(start));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(1, 0, 0).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(2, 0, 0).get()));
        assertThat(path.get().poll(), is(voxelManager.getVoxelAt(2, 1, 1).get()));
        assertThat(path.get().poll(), is(targetVoxel));
    }

}