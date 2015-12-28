package nl.civcraft.core.conf;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Spatial;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugGuiConfiguration {


    @Bean
    public BitmapText fpsText(BitmapFont guiFont) {
        BitmapText fpsText = new BitmapText(guiFont, false);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        fpsText.setCullHint(Spatial.CullHint.Never);
        return fpsText;
    }

    @Bean
    public BitmapText logMessageText(BitmapFont guiFont) {
        BitmapText logMessageText = new BitmapText(guiFont, false);
        logMessageText.setLocalTranslation(0, logMessageText.getLineHeight() * 2, 0);
        logMessageText.setText("");
        logMessageText.setCullHint(Spatial.CullHint.Never);
        return logMessageText;
    }


}
