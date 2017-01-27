package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.test.util.MocksCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Bob on 27-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemComponentTest {

    public static final String TEST_TYPE = "testType";
    private static MocksCollector mocksCollector = new MocksCollector();
    private ItemComponent underTest;
    @Spy
    private GameObject testGameObject = new GameObject();

    @Before
    public void setUp() throws Exception {
        underTest = new ItemComponent(TEST_TYPE);
        testGameObject.addComponent(underTest);
        verify(testGameObject).addComponent(underTest);
    }

    @After
    public void tearDown() throws Exception {
        mocksCollector.validateMocks();
    }

    @Test
    public void gameObjectIsSet() {
        assertThat(underTest.getGameObject(), is(testGameObject));
    }


    @Test
    public void setInInventory_gameObjectIsNotified() throws Exception {
        underTest.setInInventory(true);
        underTest.setInInventory(false);
        verify(testGameObject, times(2)).changed();
    }


}