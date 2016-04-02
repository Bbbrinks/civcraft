package nl.civcraft.desktop;

import nl.civcraft.core.CivCraftApplication;
import nl.civcraft.core.conf.InitialConfiguration;
import nl.civcraft.desktop.conf.DesktopConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
class Civcraft {

    public static void main(String[] args){
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(InitialConfiguration.class, DesktopConfiguration.class);
        CivCraftApplication app = appContext.getBean(CivCraftApplication.class);
    }
}
