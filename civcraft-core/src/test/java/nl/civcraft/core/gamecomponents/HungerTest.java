package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.worldgeneration.Apple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static nl.civcraft.test.util.ThrowableAssertion.assertThrown;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class HungerTest {
    private Hunger underTest;
    @Mock
    private Civvy testCivvy;
    private GameObject gameObject;

    @Before
    public void setUp() throws Exception {
        gameObject = new GameObject();
        gameObject.addComponent(testCivvy);
        underTest = new Hunger();
        underTest.addTo(gameObject);
    }

    @Test
    public void testAddTo_nonCivvy() throws Exception {
        assertThrown(() -> underTest.addTo(new GameObject())).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testHandleTick_StarveToDeath() {
        for (int i = 0; i < 3200; i++) {
            underTest.handleTick();
        }
        verify(testCivvy).kill();
    }

    @Test
    public void testHandleTick_recoverFromStarvation() {
        for (int i = 0; i < 3100; i++) {
            underTest.handleTick();
        }
        underTest.eat(new Apple());
        underTest.eat(new Apple());
        for (int i = 0; i < 200; i++) {
            underTest.handleTick();
        }
        verify(testCivvy, never()).kill();
    }

}