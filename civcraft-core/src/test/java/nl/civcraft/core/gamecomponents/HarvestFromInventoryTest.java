package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import nl.civcraft.core.npc.Civvy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static nl.civcraft.test.util.ThrowableAssertion.assertThrown;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class HarvestFromInventoryTest {
    private HarvestFromInventory underTest;
    private GameObject testGameObject;
    @Mock
    private Inventory inventory;
    @Mock
    private Civvy testCivvy;
    private Item testItem;

    @Before
    public void setUp() throws Exception {
        testGameObject = new GameObject();
        testGameObject.addComponent(inventory);
        underTest = new HarvestFromInventory();
        underTest.addTo(testGameObject);
        testItem = new Item();
    }

    @Test
    public void harvest_returnsFirstItem() throws Exception {
        when(inventory.getFirstItem()).thenReturn(Optional.of(testItem));
        Optional<Item> harvest = underTest.harvest(testCivvy);
        assertThat(harvest.isPresent(), is(true));
        assertThat(harvest.get(), is(testItem));
    }

    @Test
    public void harvest_noInventory() throws Exception {
        testGameObject.removeComponent(inventory);
        assertThrown(() -> underTest.harvest(testCivvy)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void addTo_noInventory() throws Exception {
        testGameObject.removeComponent(inventory);
        assertThrown(() -> underTest.addTo(testGameObject)).isInstanceOf(IllegalStateException.class);
    }

}