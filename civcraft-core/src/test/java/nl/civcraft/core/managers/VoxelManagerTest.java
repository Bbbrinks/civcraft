package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.VoxelRenderer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

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
    @Mock
    private Voxel voxel;

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
        gameObject.addComponent(voxel);
        gameObject.addComponent(voxel);
        return gameObject;
    }

    @Test
    public void testAddVxoel_doesntAddNonNeighbourGameObject() {
        GameObject voxel = createVoxel(Vector3f.ZERO);
        voxel.removeComponent(voxel.getComponent(Neighbour.class).get());
        assertThrown(() -> underTest.addVoxel(new GameObject())).isInstanceOf(IllegalStateException.class);
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
    //</editor-fold>

    @Test
    public void testAddVoxel_replacesExistingVoxel() {
        GameObject replaced = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(replaced);
        GameObject expected = createVoxel(Vector3f.ZERO);
        underTest.addVoxel(expected);
        Optional<GameObject> voxelAt = underTest.getVoxelAt(Vector3f.ZERO);
        assertThat(voxelAt, optionalWithValue(is(expected)));
    }

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
        underTest.addVoxel(expected);
        Optional<GameObject> groundAt = underTest.getGroundAt(new Vector3f(0, 20, 0).mult(-1), 20);
        assertThat(groundAt, optionalWithValue(is(expected)));
    }
}