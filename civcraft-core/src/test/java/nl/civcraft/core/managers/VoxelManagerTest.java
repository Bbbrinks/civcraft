package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.VoxelRenderer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static nl.civcraft.test.util.ThrowableAssertion.assertThrown;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by Bob on 17-2-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class VoxelManagerTest {


    private VoxelManager underTest;
    @Mock
    private VoxelRenderer.StaticVoxelRendererFactory voxelRenderer;

    @Before
    public void setUp() throws Exception {
        underTest = new VoxelManager();
    }

    //<editor-fold desc="addVoxel">
    @Test
    public void testAddVxoel_doesntAddNonVoxelGameObject() {
        GameObject voxel = createVoxel(Vector3f.ZERO);
        voxel.removeComponent(voxel.getComponent(Voxel.class).get());
        assertThrown(() -> underTest.addVoxel(voxel)).isInstanceOf(IllegalStateException.class);
    }

    private GameObject createVoxel(Vector3f translation) {
        GameObject gameObject = new GameObject(new Transform(translation));
        gameObject.addComponent(new Voxel("test", underTest));
        return gameObject;
    }

    @Test
    public void testAddVoxel_addsVoxel() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(expected);
        Optional<GameObject> voxelAt = underTest.getVoxelAt(Vector3f.ZERO);
        assertThat(voxelAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testAddVoxel_addNegativeCoords() {
        GameObject expected = createVoxel(new Vector3f(-100, -100, -100));
        underTest.addVoxel(expected);
        Optional<GameObject> voxelAt = underTest.getVoxelAt(new Vector3f(-100, -100, -100));
        assertThat(voxelAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testAddVoxel_replacesExistingVoxel() {
        GameObject replaced = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(replaced);
        GameObject expected = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(expected);
        Optional<GameObject> voxelAt = underTest.getVoxelAt(Vector3f.ZERO);
        assertThat(voxelAt, optionalWithValue(is(expected)));
    }
    //</editor-fold>


    //<editor-fold desc="getGroundAt">
    @Test
    public void testGetGroundAt_returnsGroundAtBelow() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(Vector3f.UNIT_Y, 1);
        assertThat(groundAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testGetGroundAt_returnsGroundAtBelowHighMax() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(new Vector3f(0, 20, 0), 20);
        assertThat(groundAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testGetGroundAt_returnsGroundAtAbove() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        createVoxel(Vector3f.UNIT_Y.mult(-1));
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(Vector3f.UNIT_Y.mult(-1), 1);
        assertThat(groundAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testGetGroundAt_returnsGroundAtAboveHighMax() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        for (int i = -1; i > -21; i--) {
            createVoxel(new Vector3f(0, i, 0));
        }
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(new Vector3f(0, 20, 0).mult(-1), 20);
        assertThat(groundAt, optionalWithValue(is(expected)));
    }

    @Test
    public void testGetGroundAt_returnsGroundAtAboveGroundToHeigh() {
        GameObject expected = createVoxel(Vector3f.ZERO);
        for (int i = -1; i > -21; i--) {
            createVoxel(new Vector3f(0, i, 0));
        }
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(new Vector3f(0, 20, 0).mult(-1), 5);
        assertThat(groundAt, emptyOptional());
    }

    @Test
    public void testGetGroundAt_groundNotFound() {
        Optional<GameObject> groundAt = underTest.getGroundAt(new Vector3f(0, 0, 0).mult(-1), 20);
        assertThat(groundAt, emptyOptional());
    }
    //</editor-fold>

    @Test
    public void testClear() {
        underTest.addVoxel(createVoxel(Vector3f.ZERO));
        underTest.clear();
        Optional<GameObject> voxelAt = underTest.getVoxelAt(Vector3f.ZERO);
        assertThat(voxelAt, emptyOptional());
    }
}