package nl.civcraft.core.conf;

import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"nl.civcraft"})
public class InitialConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Scheduler scheduler() {
        return new SingleScheduler();
    }
}
