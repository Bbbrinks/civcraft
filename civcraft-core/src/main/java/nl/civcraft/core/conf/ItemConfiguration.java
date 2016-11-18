package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.managers.GameObjectManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ItemConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("item")
    public GameObjectManager itemManager(ApplicationEventPublisher applicationEventPublisher) {
        GameObjectManager gameObjectManager = new GameObjectManager(applicationEventPublisher);
        gameObjectManager.registerComponent(new ItemComponent.Factory());
        gameObjectManager.registerComponent(new Haulable.Factory());
        return gameObjectManager;
    }
}
