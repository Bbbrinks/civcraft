package nl.civcraft.core.conf;

import com.jme3.input.*;
import com.jme3.system.JmeContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfiguration {

    @Bean
    public InputManager inputManager(MouseInput mouse, KeyInput keys, JoyInput joystick, TouchInput touch) {
        return new InputManager(mouse, keys, joystick, touch);
    }

    @Bean
    public MouseInput mouse(JmeContext context) {
        MouseInput mouseInput = context.getMouseInput();
        if (mouseInput != null) {
            mouseInput.initialize();
        }
        return mouseInput;
    }

    @Bean
    public KeyInput keys(JmeContext context) {
        KeyInput keyInput = context.getKeyInput();
        if (keyInput != null) {
            keyInput.initialize();
        }
        return keyInput;
    }

    @Bean
    public JoyInput joystick(JmeContext context) {
        return null;
    }

    @Bean
    public TouchInput touch(JmeContext context) {
        TouchInput touchInput = context.getTouchInput();
        if (touchInput != null) {
            touchInput.initialize();
        }
        return touchInput;
    }
}
