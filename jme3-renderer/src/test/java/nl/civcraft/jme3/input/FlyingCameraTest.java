package nl.civcraft.jme3.input;

import com.jme3.input.InputManager;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Bob on 18-9-2016.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class FlyingCameraTest {

    private FlyingCamera underTest;
    @Mock
    private InputManager inputManager;

    private Camera camera;

    @Before
    public void setUp() throws Exception {
        camera = new Camera(1, 1);
        underTest = new FlyingCamera(camera, inputManager);
    }

    @Test
    public void registerWithInput() throws Exception {
        underTest.registerWithInput();
        verify(inputManager, times(9)).addMapping(anyString(), any(Trigger.class));
        verify(inputManager, atLeastOnce()).addListener(eq(underTest), anyVararg());
    }

    @Test
    public void unregisterInput() throws Exception {
        when(inputManager.hasMapping(anyString())).thenReturn(true);
        underTest.unregisterInput();
        verify(inputManager, times(13)).deleteMapping(anyString());
        verify(inputManager, atLeastOnce()).removeListener(eq(underTest));
    }

    @Test
    public void onAnalog_backward() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_BACKWARD, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(0, 0, -10f)));
    }

    @Test
    public void onAnalog_zoomIn() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_ZOOM_IN, 1, 1);
        assertThat(camera.getFrustumBottom(), is(-0.5109564f));
        assertThat(camera.getFrustumTop(), is(0.5109564f));
        assertThat(camera.getFrustumLeft(), is(-0.5109564f));
        assertThat(camera.getFrustumRight(), is(0.5109564f));
    }

    @Test
    public void onAnalog_zoomOut() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_ZOOM_OUT, 1, 1);
        assertThat(camera.getFrustumBottom(), is(-0.4891388F));
        assertThat(camera.getFrustumTop(), is(0.4891388F));
        assertThat(camera.getFrustumLeft(), is(-0.4891388F));
        assertThat(camera.getFrustumRight(), is(0.4891388F));
    }

    @Test
    public void onAnalog_forward() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_FORWARD, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(0, 0, 10f)));
    }

    @Test
    public void onAnalog_strafeLeft() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_STRAFE_LEFT, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(10f, 0, 0)));
    }

    @Test
    public void onAnalog_strafeRight() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_STRAFE_RIGHT, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(-10f, 0, 0)));
    }

    @Test
    public void onAnalog_rise() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_RISE, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(0, 10f, 0)));
    }

    @Test
    public void onAnalog_lower() throws Exception {
        underTest.onAnalog(FlyingCamera.FLYCAM_LOWER, 1, 1);
        assertThat(camera.getLocation(), is(new Vector3f(0, -10f, 0)));
    }

    @Test
    public void onAnalog_down() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, true, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_DOWN, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(0, -0.8414711f, 0.54030216f)));
    }

    @Test
    public void onAnalog_up() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, true, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_UP, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(0, 0.8414711f, 0.54030216f)));
    }

    @Test
    public void onAnalog_left() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, true, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_LEFT, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(0.8414711f, 0, 0.54030216f)));
    }

    @Test
    public void onAnalog_right() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, true, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_RIGHT, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(-0.8414711f, -0.0f, 0.54030216f)));
    }

    @Test
    public void onAction_toggleDragOn() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, true, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_RIGHT, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(-0.8414711f, -0.0f, 0.54030216f)));
    }


    @Test
    public void onAction_toggleDragOff() throws Exception {
        underTest.onAction(FlyingCamera.FLYCAM_ROTATE_DRAG, false, 1);
        underTest.onAnalog(FlyingCamera.FLYCAM_RIGHT, 1, 1);
        assertThat(camera.getDirection(), is(new Vector3f(0.0f, 0.0f, 1.0f)));
    }


}