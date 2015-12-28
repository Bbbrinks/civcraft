package nl.civcraft.core.input;

import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * Created by Bob on 28-12-2015.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class FlyingCameraTest {

    private FlyingCamera underTest;
    @Mock
    private Camera camera;
    @Mock
    private InputManager inputManager;


    @Before
    public void setUp() throws Exception {
        underTest = new FlyingCamera();
        when(camera.getUp()).thenReturn(new Vector3f());
        //TODO after intellij 16 refactor so camera can be injected in constructor
        underTest.init(camera, inputManager);
    }

    @Test
    public void testRegisterWithInput_actionsAreBound() throws Exception {
        underTest.registerWithInput();
        verify(inputManager, atLeastOnce()).addMapping(anyString(), any());
        verify(inputManager).addListener(eq(underTest), Matchers.<String>anyVararg());
    }


    @Test
    public void testSetEnabled_cursorVisibleWhenDisabling() throws Exception {
        underTest.setEnabled(false);
        verify(inputManager).setCursorVisible(true);
    }

    @Test
    public void testSetEnabled_doesNothingOnTrue() throws Exception {
        underTest.setEnabled(true);
        verify(inputManager, never()).setCursorVisible(anyBoolean());
    }

    @Test
    public void testUnregisterInput_onlyRemovesRegisteredMappings() throws Exception {
        when(inputManager.hasMapping(anyString())).thenReturn(false);
        underTest.unregisterInput();
        verify(inputManager, never()).deleteMapping(anyString());
        when(inputManager.hasMapping(anyString())).thenReturn(true);
        underTest.unregisterInput();
        verify(inputManager, atLeastOnce()).deleteMapping(anyString());
    }

    @Test
    public void testUnregisterInput_unbindsListener() throws Exception {
        underTest.unregisterInput();
        verify(inputManager, atLeastOnce()).removeListener(eq(underTest));
    }

    @Test
    public void testOnAnalog() throws Exception {

    }

    @Test
    public void testOnAction() throws Exception {

    }

    @Test
    public void testInit() throws Exception {

    }
}