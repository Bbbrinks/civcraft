/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package nl.civcraft.core.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlyingCamera implements AnalogListener, ActionListener {

    protected static final String[] MAPPINGS = new String[]{
            "FLYCAM_Left",
            "FLYCAM_Right",
            "FLYCAM_Up",
            "FLYCAM_Down",

            "FLYCAM_StrafeLeft",
            "FLYCAM_StrafeRight",
            "FLYCAM_Forward",
            "FLYCAM_Backward",

            "FLYCAM_ZoomIn",
            "FLYCAM_ZoomOut",
            "FLYCAM_RotateDrag",

            "FLYCAM_Rise",
            "FLYCAM_Lower",

            "FLYCAM_InvertY"
    };
    private final float moveSpeed = 10f;
    private final Camera camera;
    private final InputManager inputManager;
    private Vector3f initialUpVec;
    private boolean enabled = true;
    private boolean canRotate = false;
    private boolean invertY = false;


    @Autowired
    public FlyingCamera(Camera camera, InputManager inputManager) {
        this.camera = camera;
        initialUpVec = this.camera.getUp().clone();
        this.inputManager = inputManager;
    }

    public void registerWithInput() {

        // both mouse and button - rotation of camera
        inputManager.addMapping("FLYCAM_Left", new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping("FLYCAM_Right", new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping("FLYCAM_Up", new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping("FLYCAM_Down", new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("FLYCAM_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, MAPPINGS);
    }

    /**
     * @param enable If false, the camera will ignore input.
     */
    public void setEnabled(boolean enable) {
        if (!enable) {
            inputManager.setCursorVisible(true);
        }
    }

    /**
     * Registers the FlyingCamera to receive input events from the provided
     * Dispatcher.
     */

    public void unregisterInput() {
        for (String s : MAPPINGS) {
            if (inputManager.hasMapping(s)) {
                inputManager.deleteMapping(s);
            }
        }
        inputManager.removeListener(this);

    }

    public void onAnalog(String name, float value, float tpf) {
        if (!enabled)
            return;

        switch (name) {
            case "FLYCAM_Left":
                rotateCamera(value, initialUpVec);
                break;
            case "FLYCAM_Right":
                rotateCamera(-value, initialUpVec);
                break;
            case "FLYCAM_Up":
                rotateCamera(-value * (invertY ? -1 : 1), camera.getLeft());
                break;
            case "FLYCAM_Down":
                rotateCamera(value * (invertY ? -1 : 1), camera.getLeft());
                break;
            case "FLYCAM_Forward":
                moveCamera(value, false);
                break;
            case "FLYCAM_Backward":
                moveCamera(-value, false);
                break;
            case "FLYCAM_StrafeLeft":
                moveCamera(value, true);
                break;
            case "FLYCAM_StrafeRight":
                moveCamera(-value, true);
                break;
            case "FLYCAM_Rise":
                riseCamera(value);
                break;
            case "FLYCAM_Lower":
                riseCamera(-value);
                break;
            case "FLYCAM_ZoomIn":
                zoomCamera(value);
                break;
            case "FLYCAM_ZoomOut":
                zoomCamera(-value);
                break;
            default:
                break;
        }
    }

    private void rotateCamera(float value, Vector3f axis) {

        if (!canRotate) {
            return;
        }


        Matrix3f mat = new Matrix3f();
        float rotationSpeed = 1f;
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = camera.getUp();
        Vector3f left = camera.getLeft();
        Vector3f dir = camera.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        camera.setAxes(q);
    }

    private void moveCamera(float value, boolean sideways) {
        Vector3f vel = new Vector3f();
        Vector3f pos = camera.getLocation().clone();

        if (sideways) {
            camera.getLeft(vel);
        } else {
            camera.getDirection(vel);
        }
        vel.multLocal(value * moveSpeed);

        pos.addLocal(vel);

        camera.setLocation(pos);
    }

    private void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value * moveSpeed, 0);
        Vector3f pos = camera.getLocation().clone();

        pos.addLocal(vel);

        camera.setLocation(pos);
    }

    private void zoomCamera(float value) {
        // derive fovY value
        float h = camera.getFrustumTop();
        float w = camera.getFrustumRight();
        float aspect = w / h;

        float near = camera.getFrustumNear();

        float fovY = FastMath.atan(h / near)
                / (FastMath.DEG_TO_RAD * .5f);
        float zoomSpeed = 10f;
        float newFovY = fovY + value * 0.1f * zoomSpeed;
        if (newFovY > 0f) {
            // Don't let the FOV go zero or negative.
            fovY = newFovY;
        }

        h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
        w = h * aspect;

        camera.setFrustumTop(h);
        camera.setFrustumBottom(-h);
        camera.setFrustumLeft(-w);
        camera.setFrustumRight(w);
    }

    public void onAction(String name, boolean value, float tpf) {
        if (!enabled)
            return;

        if (name.equals("FLYCAM_RotateDrag")) {
            canRotate = value;
            inputManager.setCursorVisible(!value);
        } else if (name.equals("FLYCAM_InvertY")) {
            // Toggle on the up.
            if (!value) {
                invertY = !invertY;
            }
        }
    }


}
