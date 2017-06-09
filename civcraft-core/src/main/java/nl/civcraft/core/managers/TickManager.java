package nl.civcraft.core.managers;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


public class TickManager {

    private final Observable<Long> tick;

    @Inject
    public TickManager(Scheduler scheduler) {
        tick = Observable.interval(50, TimeUnit.MILLISECONDS, scheduler);
    }

    public Observable<Long> getTick() {
        return tick;
    }
}
