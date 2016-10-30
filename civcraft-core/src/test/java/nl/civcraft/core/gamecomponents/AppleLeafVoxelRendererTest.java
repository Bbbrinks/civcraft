package nl.civcraft.core.gamecomponents;

import com.jme3.scene.Node;
import nl.civcraft.core.model.Voxel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

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
    private Node filledBlock;
    @Mock
    private Node emptyBlock;
    private Voxel testGameObject;
    @Mock
    private Inventory inventory;
    @Mock
    private ApplicationEventPublisher publisher;

    @Before
    public void setUp() throws Exception {
        when(filledBlock.clone()).thenReturn(filledBlock);
        when(emptyBlock.clone()).thenReturn(emptyBlock);

        testGameObject = new Voxel(0, 0, 0, "test", publisher);
        testGameObject.addComponent(inventory);
        underTest = new AppleLeafVoxelRenderer(emptyBlock, filledBlock);
        underTest.addTo(testGameObject);
    }

    @Test
    public void getNode_noInventory() throws Exception {
        testGameObject.removeComponent(inventory);
        assertThrown(() -> underTest.getNode()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getNode_empty() throws Exception {
        when(inventory.isEmpty()).thenReturn(true);
        Node node = underTest.getNode();
        assertThat(node, is(emptyBlock));
    }

    @Test
    public void getNode_filled() throws Exception {
        when(inventory.isEmpty()).thenReturn(false);
        Node node = underTest.getNode();
        assertThat(node, is(filledBlock));
    }


}