package nl.civcraft.opengl.interaction;

import com.google.inject.Singleton;
import nl.civcraft.opengl.engine.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class KeyboardInputManager {

    private final Map<Integer,Consumer<Float>> keyBinds = new HashMap<>();

    public void bindKey(int key, Consumer<Float> callBack){
        this.keyBinds.put(key, callBack);
    }

    public void update(Window window,
                       float elapsedTime) {
        keyBinds.entrySet().stream().filter(keyBind -> window.isKeyPressed(keyBind.getKey())).forEach(keyBind -> keyBind.getValue().accept(elapsedTime));
    }

}
