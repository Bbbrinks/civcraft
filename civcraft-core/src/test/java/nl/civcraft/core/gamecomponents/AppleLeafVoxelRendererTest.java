package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.VoxelFace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;

import static nl.civcraft.core.util.ThrowableAssertion.assertThrown;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class AppleLeafVoxelRendererTest {
    private AppleLeafVoxelRenderer underTest;

    @Mock
    private Map<Face, VoxelFace> filledBlock;
    @Mock
    private Map<Face, VoxelFace> emptyBlock;
    private GameObject testGameObject;
    @Mock
    private Inventory inventory;
    @Mock
    private ApplicationEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        testGameObject = new GameObject();
        testGameObject.addComponent(inventory);
        underTest = new AppleLeafVoxelRenderer(emptyBlock, filledBlock);
        underTest.addTo(testGameObject);
    }

    @Test
    public void getNode_noInventory() throws Exception {
        testGameObject.removeComponent(inventory);
        assertThrown(() -> underTest.getFaces()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getNode_empty() throws Exception {
        when(inventory.isEmpty()).thenReturn(true);
        Map<Face, VoxelFace> node = underTest.getFaces();
        assertThat(node, is(emptyBlock));
    }

    @Test
    public void getNode_filled() throws Exception {
        when(inventory.isEmpty()).thenReturn(false);
        Map<Face, VoxelFace> node = underTest.getFaces();
        assertThat(node, is(filledBlock));
    }


}