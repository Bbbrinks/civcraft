package nl.civcraft.core.managers;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class TickManager {

    private final Observable<Long> tick;

    @Autowired
    public TickManager(Scheduler scheduler) {
        tick = Observable.interval(50, TimeUnit.MILLISECONDS, scheduler);
    }

    public Observable<Long> getTick() {
        return tick;
    }
}
