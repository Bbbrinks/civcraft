package nl.civcraft.test.util;

import org.mockito.Mockito;
import org.mockito.internal.listeners.CollectCreatedMocks;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bob on 27-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class MocksCollector {
    private final List<Object> createdMocks;

    public MocksCollector() {
        createdMocks = new LinkedList<Object>();
        final MockingProgress progress = new ThreadSafeMockingProgress();
        progress.setListener(new CollectCreatedMocks(createdMocks));
    }

    public void validateMocks() {
        Mockito.verifyNoMoreInteractions(getMocks());
    }

    public Object[] getMocks() {
        return createdMocks.toArray();
    }
}