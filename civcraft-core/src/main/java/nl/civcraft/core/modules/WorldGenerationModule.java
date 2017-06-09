package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import nl.civcraft.core.worldgeneration.HeightMapGenerator;
import nl.civcraft.core.worldgeneration.HillsGenerator;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class WorldGenerationModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            Names.bindProperties(binder(), getProperties());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        bind(HeightMapGenerator.class).to(HillsGenerator.class).in(Singleton.class);
    }

    private Map<String, String> getProperties() throws IOException {
        Properties properties = new Properties();
        final URL url = WorldGenerationModule.class.getClassLoader()
                .getResource("world-generation.properties");
        if (url == null) {
            throw new IOException("Missing property file : world-generation.properties");
        }
        try (InputStream inputStream = url.openStream()) {
            properties.load(inputStream);
            return properties.stringPropertyNames().stream().collect(Collectors.toMap(s -> s,
                    properties::getProperty));
        }
    }
}
