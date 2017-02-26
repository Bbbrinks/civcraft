package nl.civcraft.core.model;

import nl.civcraft.core.gamecomponents.ItemComponent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

/**
 * Created by Bob on 26-2-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class LimitedInventoryTest {

    private LimitedInventory underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new LimitedInventory(3);
    }

    @Test
    public void testAddItem_addsTheItem() {
        GameObject testItem = createTestItem();
        underTest.addItem(testItem);

        Optional<GameObject> firstItem = underTest.getFirstItem();
        assertThat(firstItem, optionalWithValue(is(testItem)));
    }

    private static GameObject createTestItem() {
        GameObject gameObject = new GameObject();
        gameObject.addComponent(new ItemComponent("testItem"));
        return gameObject;
    }

    @Test
    public void testAddItem_failsWhenInventoryIsFull() {
        GameObject testItem = createTestItem();
        GameObject testItem1 = createTestItem();
        GameObject testItem2 = createTestItem();
        GameObject testItem3 = createTestItem();
        underTest.addItem(testItem);
        underTest.addItem(testItem2);
        underTest.addItem(testItem1);
        boolean result = underTest.addItem(testItem3);
        assertThat(result, is(false));
    }

    @Test
    public void testGetFirstItem_empty() {
        Optional<GameObject> firstItem = underTest.getFirstItem();
        assertThat(firstItem, emptyOptional());
    }

    @Test
    public void testIsEmtpy_empty() {
        assertThat(underTest.isEmpty(), is(true));
    }

    @Test
    public void testIsEmtpy_notEmpty() {
        underTest.addItem(createTestItem());
        assertThat(underTest.isEmpty(), is(false));
    }

    @Test
    public void testHasRoom_empty() {
        GameObject testItem = createTestItem();

        assertThat(underTest.hasRoom(testItem), is(true));
    }

    @Test
    public void testHasRoom_notEmpty() {
        GameObject testItem = createTestItem();
        GameObject testItem1 = createTestItem();
        GameObject testItem2 = createTestItem();
        underTest.addItem(testItem);
        underTest.addItem(testItem2);
        underTest.addItem(testItem1);
        assertThat(underTest.hasRoom(createTestItem()), is(false));
    }


}
