package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;
import org.hamcrest.core.IsCollectionContaining;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static nl.civcraft.test.util.Vector3fMatcher.isInRange;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Bob on 17-2-2017.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class NeighbourTest {
    private Neighbour underTest;
    @Mock
    private VoxelManager voxelManager;
    private GameObject testGameObject;

    @Before
    public void setUp() throws Exception {
        underTest = new Neighbour(voxelManager);
        testGameObject = new GameObject();
        when(voxelManager.getVoxelAt(any(Vector3f.class))).thenReturn(Optional.empty());
        when(voxelManager.getVoxelAt(isInRange(new Vector3f()))).thenReturn(Optional.of(testGameObject));
    }

    //<editor-fold desc="addTo">
    @Test
    public void testAddTo_GameObjectIsSet() {
        testGameObject.addComponent(underTest);
        assertThat(underTest.getGameObject(), is(testGameObject));
    }

    @Test
    public void testAddTo_topNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 1, 0), NeighbourDirection.TOP);
    }

    private void testNeighbourIsAdded(Vector3f transform, NeighbourDirection expectedDirection) {
        GameObject expectedNeighbour = addTestNeighbour(transform);
        testGameObject.addComponent(underTest);
        Optional<GameObject> actual = underTest.getNeighbour(expectedDirection);
        assertThat(actual, optionalWithValue(is(expectedNeighbour)));
        assertThat(underTest.getNeighbours().entrySet(), hasSize(1));
        assertThat(underTest.getNeighbours(), hasEntry(expectedDirection, expectedNeighbour));
    }

    @Test
    public void testAddTo_bottomNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 1, 0).mul(-1), NeighbourDirection.BOTTOM);
    }

    @Test
    public void testAddTo_leftNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(1, 0, 0).mul(-1), NeighbourDirection.LEFT);
    }

    @Test
    public void testAddTo_rightNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(1, 0, 0), NeighbourDirection.RIGHT);
    }

    @Test
    public void testAddTo_frontNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 0, 1).mul(-1), NeighbourDirection.FRONT);
    }

    @Test
    public void testAddTo_backNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 0, 1), NeighbourDirection.BACK);
    }

    @Test
    public void testDestroyed_neighboursAreRemoved() {
        addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        assertThat(underTest.getNeighbours().entrySet(), hasSize(1));
        underTest.destroyed();
        assertThat(underTest.getNeighbours().entrySet(), hasSize(0));
    }

    @Test
    public void testAddTo_leftTopNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(-1, 1, 0), NeighbourDirection.LEFT_TOP);
    }

    @Test
    public void testAddTo_rightTopNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(1, 1, 0), NeighbourDirection.RIGHT_TOP);
    }

    @Test
    public void testAddTo_leftBottomNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(-1, -1, 0), NeighbourDirection.LEFT_BOTTOM);
    }

    @Test
    public void testAddTo_rightBottomNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(1, -1, 0), NeighbourDirection.RIGHT_BOTTOM);
    }

    @Test
    public void testAddTo_frontTopNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 1, -1), NeighbourDirection.FRONT_TOP);
    }

    @Test
    public void testAddTo_backTopNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, 1, 1), NeighbourDirection.BACK_TOP);
    }

    @Test
    public void testAddTo_frontBottomNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, -1, -1), NeighbourDirection.FRONT_BOTTOM);
    }

    @Test
    public void testAddTo_backBottomNeighbourIsAdded() {
        testNeighbourIsAdded(new Vector3f(0, -1, 1), NeighbourDirection.BACK_BOTTOM);
    }
    //</editor-fold>

    //<editor-fold desc="destroyed">
    @Test
    public void testDestroyed_gameObjectIsCleared() {
        testGameObject.addComponent(underTest);
        underTest.destroyed();
        assertThat(underTest.getGameObject(), is(nullValue()));
    }

    private GameObject addTestNeighbour(Vector3f location) {
        GameObject gameObject = new GameObject(new Matrix4f().translate(location));
        when(voxelManager.getVoxelAt(isInRange(location))).thenReturn(Optional.of(gameObject));
        gameObject.addComponent(new Neighbour(voxelManager));
        return gameObject;
    }
    //</editor-fold>

    //<editor-fold desc="getEntrableNeighbours">
    @Test
    public void testGetEnterableNeighbours_returnsDirectNeighbourWithoutTop() {
        GameObject rightNeighbour = addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        List<GameObject> enterableNeighbours = underTest.getEnterableNeighbours();
        assertThat(enterableNeighbours, IsCollectionContaining.hasItems(rightNeighbour));
    }

    @Test
    public void testGetEnterableNeighbours_returnsNoDirectNeighbourWithTop() {
        GameObject rightNeighbour = addTestNeighbour(new Vector3f(1, 0, 0));
        GameObject rightTopNeighbour = addTestNeighbour(new Vector3f(1, 1, 0));
        testGameObject.addComponent(underTest);
        List<GameObject> enterableNeighbours = underTest.getEnterableNeighbours();
        assertThat(enterableNeighbours, hasSize(1));
        assertThat(enterableNeighbours, not(hasItem(rightNeighbour)));
        assertThat(enterableNeighbours, hasItem(rightTopNeighbour));
    }

    @Test
    public void testGetEnterableNeighbours_returnsDownVerticalDirectNeighbourWithoutTop() {
        GameObject downNeighbour = addTestNeighbour(new Vector3f(-1, -1, 0));
        testGameObject.addComponent(underTest);
        List<GameObject> enterableNeighbours = underTest.getEnterableNeighbours();
        assertThat(enterableNeighbours, hasSize(1));
        assertThat(enterableNeighbours, hasItem(downNeighbour));
    }

    @Test
    public void testGetEnterableNeighbours_returnsNotDownVerticalDirectNeighbourWithTop() {
        GameObject downNeighbour = addTestNeighbour(new Vector3f(-1, -1, 0));
        addTestNeighbour(new Vector3f(-1, 0, 0));
        testGameObject.addComponent(underTest);
        List<GameObject> enterableNeighbours = underTest.getEnterableNeighbours();
        assertThat(enterableNeighbours, hasSize(1));
        assertThat(enterableNeighbours, not(hasItem(downNeighbour)));
    }

    //</editor-fold>

    //<editor-fold desc="static methods">
    @Test
    public void testStaticGetNeighbours() {
        GameObject rightNeighbour = addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        Map<NeighbourDirection, GameObject> neighbours = Neighbour.getNeighbours(testGameObject);
        assertThat(neighbours, hasEntry(NeighbourDirection.RIGHT, rightNeighbour));
    }

    @Test
    public void testStaticGetNeighbour() {
        GameObject rightNeighbour = addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        Optional<GameObject> neighbour = Neighbour.getNeighbour(testGameObject, NeighbourDirection.RIGHT);
        assertThat(neighbour, optionalWithValue(is(rightNeighbour)));
    }

    @Test
    public void testStaticHasNeighbour_true() {
        addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        assertThat(Neighbour.hasNeighbour(testGameObject, NeighbourDirection.RIGHT), is(true));
    }

    @Test
    public void testStaticHasNeighbour_false() {
        addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        assertThat(Neighbour.hasNeighbour(testGameObject, NeighbourDirection.LEFT), is(false));
    }
    //</editor-fold>

    @Test
    public void testGetNeighbours() {
        GameObject right = addTestNeighbour(new Vector3f(1, 0, 0));
        GameObject left = addTestNeighbour(new Vector3f(1, 0, 0).mul(-1));
        testGameObject.addComponent(underTest);
        Map<NeighbourDirection, GameObject> neighbours = underTest.getNeighbours(NeighbourDirection.RIGHT, NeighbourDirection.TOP);
        assertThat(neighbours, hasEntry(NeighbourDirection.RIGHT, right));
        assertThat(neighbours, not(hasKey(NeighbourDirection.LEFT)));
        assertThat(neighbours, not(hasKey(NeighbourDirection.TOP)));
    }

    @Test
    public void testGetDirectNeighbours() {
        GameObject right = addTestNeighbour(new Vector3f(1, 0, 0));
        testGameObject.addComponent(underTest);
        Map<NeighbourDirection, GameObject> directNeighbours = underTest.getDirectNeighbours();
        assertThat(directNeighbours, hasEntry(NeighbourDirection.RIGHT, right));
    }

    @Test
    public void testFactory() {
        Neighbour.Factory factory = new Neighbour.Factory(voxelManager);
        assertThat(factory.build(), isA(Neighbour.class));
        assertThat(factory.getComponentType(), is(Neighbour.class));
    }
}