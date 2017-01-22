package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.managers.PrefabManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class Items {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("apple")
    public PrefabManager apple(@Qualifier("item") PrefabManager itemManager, ApplicationEventPublisher applicationEventPublisher) {
        PrefabManager appleManager = new PrefabManager(applicationEventPublisher, itemManager);
        appleManager.registerComponent(new ItemComponent.Factory("apple"));
        return appleManager;
    }
}
