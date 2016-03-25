package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Bob on 28-12-2015.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class CivCraftFlyCamStateTest {

    private CivCraftFlyCamState underTest;

    @Mock
    private FlyingCamera camera;
    @Mock
    private AppStateManager stateManager;
    @Mock
    private Application app;
    @Mock
    private Spatial rootNode;
    @Mock
    private AssetManager assetManager;


    @Before
    public void setUp() throws Exception {
        underTest = new CivCraftFlyCamState(camera, rootNode, assetManager);
    }

    @Test
    public void testInitialize_flyCamIsInitialized() throws Exception {
        underTest.initialize(stateManager, app);
        verify(camera).init(any(Camera.class), any(InputManager.class));
        verify(camera).registerWithInput();
    }

    @Test
    public void testCleanup() throws Exception {
        underTest.cleanup();
        verify(camera).unregisterInput();
    }
}