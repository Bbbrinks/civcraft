package nl.civcraft.jme3.input;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CivCraftFlyCamState {

    private final FlyingCamera flyCam;


    @Autowired
    public CivCraftFlyCamState(FlyingCamera camera) {
        this.flyCam = camera;
        flyCam.registerWithInput();
    }

    public void cleanup() {
        flyCam.unregisterInput();
    }
}
