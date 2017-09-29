package nl.civcraft.core.managers;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Bob on 22-9-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class PrefabManagerManager {

    private final Injector injector;

    @Inject
    public PrefabManagerManager(Injector injector) {
        this.injector = injector;
    }

    public PrefabManager get(String name) {

        return injector.getInstance(Key.get(PrefabManager.class, Names.named(name)));
    }
}
