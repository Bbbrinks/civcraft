package nl.civcraft.core.model.events;

import com.jme3.math.Transform;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class SelectionEvent extends ApplicationEvent {

    private final List<Transform> selection;

    public SelectionEvent(List<Transform> selection,
                          Object source) {
        super(source);
        this.selection = selection;
    }

    public List<Transform> getSelection() {
        return selection;
    }
}
