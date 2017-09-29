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
package nl.civcraft.jme3.nl.civcraft.jme3.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import javax.inject.Inject;


public class FlyingCamera implements AnalogListener, ActionListener {

    static final String FLYCAM_LEFT = "FLYCAM_Left";
    static final String FLYCAM_RIGHT = "FLYCAM_Right";
    static final String FLYCAM_UP = "FLYCAM_Up";
    static final String FLYCAM_DOWN = "FLYCAM_Down";
    static final String FLYCAM_STRAFE_LEFT = "FLYCAM_StrafeLeft";
    static final String FLYCAM_STRAFE_RIGHT = "FLYCAM_StrafeRight";
    static final String FLYCAM_FORWARD = "FLYCAM_Forward";
    static final String FLYCAM_BACKWARD = "FLYCAM_Backward";
    static final String FLYCAM_ZOOM_IN = "FLYCAM_ZoomIn";
    static final String FLYCAM_ZOOM_OUT = "FLYCAM_ZoomOut";
    static final String FLYCAM_ROTATE_DRAG = "FLYCAM_RotateDrag";
    static final String FLYCAM_RISE = "FLYCAM_Rise";
    static final String FLYCAM_LOWER = "FLYCAM_Lower";
    private static final String[] MAPPINGS = new String[]{
            FLYCAM_LEFT,
            FLYCAM_RIGHT,
            FLYCAM_UP,
            FLYCAM_DOWN,

            FLYCAM_STRAFE_LEFT,
            FLYCAM_STRAFE_RIGHT,
            FLYCAM_FORWARD,
            FLYCAM_BACKWARD,

            FLYCAM_ZOOM_IN,
            FLYCAM_ZOOM_OUT,
            FLYCAM_ROTATE_DRAG,

            FLYCAM_RISE,
            FLYCAM_LOWER
    };
    private static final float MOVE_SPEED = 10f;
    private final Camera camera;
    private final InputManager inputManager;
    private final Vector3f initialUpVec;
    private boolean canRotate = false;


    @Inject
    public FlyingCamera(Camera camera, InputManager inputManager) {
        this.camera = camera;
        initialUpVec = this.camera.getUp().clone();
        this.inputManager = inputManager;
    }

    public void registerWithInput() {

        // both mouse and button - rotation of camera
        inputManager.addMapping(FLYCAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping(FLYCAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping(FLYCAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping(FLYCAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping(FLYCAM_ZOOM_IN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(FLYCAM_ZOOM_OUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(FLYCAM_ROTATE_DRAG, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        // keyboard only WASD for movement and WZ for rise/lower height
        inputManager.addMapping(FLYCAM_STRAFE_LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(FLYCAM_STRAFE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(FLYCAM_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(FLYCAM_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(FLYCAM_RISE, new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping(FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_Z));

        inputManager.addListener(this, MAPPINGS);
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

    @Override
    public void onAnalog(String name, float value, float tpf) {
        switch (name) {
            case FLYCAM_LEFT:
                rotateCamera(value, initialUpVec);
                break;
            case FLYCAM_RIGHT:
                rotateCamera(-value, initialUpVec);
                break;
            case FLYCAM_UP:
                rotateCamera(-value, camera.getLeft());
                break;
            case FLYCAM_DOWN:
                rotateCamera(value, camera.getLeft());
                break;
            case FLYCAM_FORWARD:
                moveCamera(value, false);
                break;
            case FLYCAM_BACKWARD:
                moveCamera(-value, false);
                break;
            case FLYCAM_STRAFE_LEFT:
                moveCamera(value, true);
                break;
            case FLYCAM_STRAFE_RIGHT:
                moveCamera(-value, true);
                break;
            case FLYCAM_RISE:
                riseCamera(value);
                break;
            case FLYCAM_LOWER:
                riseCamera(-value);
                break;
            case FLYCAM_ZOOM_IN:
                zoomCamera(value);
                break;
            case FLYCAM_ZOOM_OUT:
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
        vel.multLocal(value * MOVE_SPEED);

        pos.addLocal(vel);

        camera.setLocation(pos);
    }

    private void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value * MOVE_SPEED, 0);
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

    @Override
    public void onAction(String name, boolean value, float tpf) {
        if (FLYCAM_ROTATE_DRAG.equals(name)) {
            canRotate = value;
            inputManager.setCursorVisible(!value);
        }
    }


}
