package nl.civcraft.core.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by Bob on 17-2-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class NeighbourDirectionTest {

    @Test
    public void fromFace_top() throws Exception {
        NeighbourDirection neighbourDirection = NeighbourDirection.fromFace(Face.TOP);
        assertThat(neighbourDirection, is(NeighbourDirection.TOP));
    }
}