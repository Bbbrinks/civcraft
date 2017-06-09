package nl.civcraft.core;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class SystemEventPublisher {


    private final Subject<Float> publisher;

    public SystemEventPublisher() {
        this.publisher = PublishSubject.create();
    }

    public Subject<Float> getPublisher() {
        return publisher;
    }
}
