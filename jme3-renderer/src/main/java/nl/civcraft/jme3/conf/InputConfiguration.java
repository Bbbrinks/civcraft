package nl.civcraft.jme3.conf;

import com.jme3.input.*;
import com.jme3.system.JmeContext;
import nl.civcraft.core.Civcraft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfiguration {

    @Bean
    public InputManager inputManager(Civcraft civcraft) {
        return civcraft.getInputManager();
    }

    @Bean
    public MouseInput mouse(JmeContext context) {
        return context.getMouseInput();
    }

    @Bean
    public KeyInput keys(JmeContext context) {
        return context.getKeyInput();
    }

    @Bean
    public JoyInput joystick(JmeContext context) {
        return context.getJoyInput();
    }

    @Bean
    public TouchInput touch(JmeContext context) {
        return context.getTouchInput();
    }
}
