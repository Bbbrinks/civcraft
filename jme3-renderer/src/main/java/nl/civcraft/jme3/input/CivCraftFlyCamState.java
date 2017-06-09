package nl.civcraft.jme3.input;

import javax.inject.Inject;

public class CivCraftFlyCamState {

    private final FlyingCamera flyCam;


    @Inject
    public CivCraftFlyCamState(FlyingCamera camera) {
        this.flyCam = camera;
        flyCam.registerWithInput();
    }

    public void cleanup() {
        flyCam.unregisterInput();
    }
}
