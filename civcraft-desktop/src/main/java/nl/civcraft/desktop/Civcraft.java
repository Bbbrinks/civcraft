package nl.civcraft.desktop;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppState;
import nl.civcraft.core.CivCraftApplication;
import nl.civcraft.core.conf.InitialConfiguration;
import nl.civcraft.desktop.conf.DesktopConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
class Civcraft {

    public static void main(String[] args){
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(InitialConfiguration.class, DesktopConfiguration.class);
        CivCraftApplication app = appContext.getBean(CivCraftApplication.class);
        Map<String, AppState> appStateList = appContext.getBeansOfType(AppState.class);
        app.start(appStateList.values().stream().collect(Collectors.toList()));
        app.getStateManager().getState(FlyCamAppState.class).getCamera().setMoveSpeed(200f);
    }
}
