package nl.civcraft.core.conf;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppState;
import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.input.GlobalInput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfiguration {
    @Bean
    public AppState debugStatsState() {
        return new DebugStatsState();
    }

    @Bean
    public AppState globalInput() {
        return new GlobalInput();
    }

    @Bean
    public AppState flyCamAppState() {
        return new FlyCamAppState();
    }

    @Bean
    public AppState debugKeysAppState() {
        return new DebugKeysAppState();
    }
}
