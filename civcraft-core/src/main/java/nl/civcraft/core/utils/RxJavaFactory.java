package nl.civcraft.core.utils;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
*   Use this for easier unit testing
 */
public class RxJavaFactory {
    public <T> Subject<T> createPublishSubject() {
        return PublishSubject.create();
    }
}
