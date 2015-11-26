package nl.civcraft.core.worldgeneration;

import com.jme3.material.Material;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class VoxelTest {


    @Mock
    private Material material;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testMerge_3by1with1by1OffSet() throws Exception {
        Voxel big = new Voxel(0, 0, 0, "brown", material);
        big.merge(new Voxel(0, 0, 1, "brown", material));
        big.merge(new Voxel(0, 0, 2, "brown", material));
        Voxel small = new Voxel(0, 1, 0, "brown", material);
        assertThat(big.canMerge(small), is(false));
        assertThat(small.canMerge(big), is(false));
    }

    @Test
    public void testMerge_3by1with1by1Centered() throws Exception {
        Voxel big = new Voxel(0, 0, 0, "brown", material);
        big.merge(new Voxel(0, 0, 1, "brown", material));
        big.merge(new Voxel(0, 0, 2, "brown", material));
        Voxel small = new Voxel(0, 1, 1, "brown", material);
        assertThat(big.canMerge(small), is(false));
        assertThat(small.canMerge(big), is(false));
    }
}