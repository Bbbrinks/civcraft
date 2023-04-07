package nl.civcraft.core.managers;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


public class TickManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Observable<Long> tick;

    @Inject
    public TickManager(Scheduler scheduler) {
        tick = Observable.interval(50, TimeUnit.MILLISECONDS, scheduler)
                .doOnError(throwable -> LOGGER.error("Error during tick update", throwable));
    }

    public Observable<Long> getTick() {
        return tick;
    }
}
