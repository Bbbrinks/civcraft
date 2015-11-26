package nl.civcraft.core.worldgeneration;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class ChunkLodOptimizerControlTest {

    private Chunk chunk;

    @Mock
    private Material material;

    @InjectMocks
    private ChunkLodOptimizerControl underTest;

    @Before
    public void setUp() {
        chunk = new Chunk(underTest);
    }

    @Test
    public void testControlUpdate_twoAdjecentVoxelsOfTheSameType() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 0, 1, "brown", material));
        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(1));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.0f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 0.5f)));
    }

    @Test
    public void testControlUpdate_twoAdjecentVoxelsDifferentTypes() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 0)));
    }

    @Test
    public void testControlUpdate_twoVoxelsOfTheSameTypeWithSpaceInBetween() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 0)));
    }

    @Test
    public void testControlUpdate_treeVoxelsInARow() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(1));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 1.0f)));
    }

    @Test
    public void testControlUpdate_treeVoxelsInACollumn() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(1, 0, 0, "white", material));
        chunk.attachChild(new Voxel(2, 0, 0, "white", material));
        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(1));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(1.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(1.0f, 0, 0)));
    }

    @Test
    public void testControlUpdate_twoVerticalRows() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 1, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 1, 1, "brown", material));
        chunk.attachChild(new Voxel(0, 1, 2, "brown", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 1, 1)));

    }

    @Test
    public void testControlUpdate_twoVerticalRowsWithAGap() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 2, 0, "white", material));
        chunk.attachChild(new Voxel(0, 2, 1, "white", material));
        chunk.attachChild(new Voxel(0, 2, 2, "white", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 1)));

    }

    @Test
    public void testControlUpdate_treeVerticalRows() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 1, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 1, 1, "brown", material));
        chunk.attachChild(new Voxel(0, 1, 2, "brown", material));
        chunk.attachChild(new Voxel(0, 2, 0, "white", material));
        chunk.attachChild(new Voxel(0, 2, 1, "white", material));
        chunk.attachChild(new Voxel(0, 2, 2, "white", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(3));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 1, 1)));

        actual = (BoundingBox) voxels.get(2).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        worldTranslation = voxels.get(2).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 1)));

    }

    @Test
    public void testControlUpdate_twoWhitesAndAnother() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 2, 0, "white", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 0)));

    }

    @Test
    public void testControlUpdate_twoWhiteRowsOneBrownRow() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 1, 0, "white", material));
        chunk.attachChild(new Voxel(0, 1, 1, "white", material));
        chunk.attachChild(new Voxel(0, 1, 2, "white", material));
        chunk.attachChild(new Voxel(0, 2, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 2, 1, "brown", material));
        chunk.attachChild(new Voxel(0, 2, 2, "brown", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(2));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(1.0f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0.5f, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 1)));
    }

    @Test
    public void testControlUpdate_twoWhiteRowsTwoWhitesInAnotherRow() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 1, 0, "white", material));
        chunk.attachChild(new Voxel(0, 1, 1, "white", material));
        chunk.attachChild(new Voxel(0, 1, 2, "white", material));
        chunk.attachChild(new Voxel(0, 2, 0, "white", material));
        chunk.attachChild(new Voxel(0, 2, 1, "white", material));
        chunk.attachChild(new Voxel(0, 2, 2, "brown", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(3));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(1.0f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0.5f, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(1.0f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 0.5f)));

        actual = (BoundingBox) voxels.get(2).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        worldTranslation = voxels.get(2).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 2)));

    }

    @Test
    public void testControlUpdate_twoWhiteRowsOneWhiteInAnotherRow() throws Exception {
        chunk.attachChild(new Voxel(0, 0, 0, "white", material));
        chunk.attachChild(new Voxel(0, 0, 1, "white", material));
        chunk.attachChild(new Voxel(0, 0, 2, "white", material));
        chunk.attachChild(new Voxel(0, 1, 0, "white", material));
        chunk.attachChild(new Voxel(0, 1, 1, "white", material));
        chunk.attachChild(new Voxel(0, 1, 2, "white", material));
        chunk.attachChild(new Voxel(0, 2, 0, "brown", material));
        chunk.attachChild(new Voxel(0, 2, 1, "white", material));
        chunk.attachChild(new Voxel(0, 2, 2, "brown", material));

        underTest.controlUpdate(0);
        List<Voxel> voxels = chunk.descendantMatches(Voxel.class);
        assertThat(voxels.size(), is(4));
        BoundingBox actual = (BoundingBox) voxels.get(0).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(1.0f));
        assertThat(actual.getZExtent(), is(1.5f));
        Vector3f worldTranslation = voxels.get(0).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 0.5f, 1.0f)));

        actual = (BoundingBox) voxels.get(1).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        worldTranslation = voxels.get(1).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 0f)));

        actual = (BoundingBox) voxels.get(2).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        worldTranslation = voxels.get(2).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 1f)));

        actual = (BoundingBox) voxels.get(3).descendantMatches(Geometry.class).get(0).getMesh().getBound();
        assertThat(actual.getXExtent(), is(0.5f));
        assertThat(actual.getYExtent(), is(0.5f));
        assertThat(actual.getZExtent(), is(0.5f));
        worldTranslation = voxels.get(3).getWorldTranslation();
        assertThat(worldTranslation, is(new Vector3f(0, 2, 2f)));

    }
}