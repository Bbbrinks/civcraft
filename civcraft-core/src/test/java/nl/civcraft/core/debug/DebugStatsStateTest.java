package nl.civcraft.core.debug;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Bob on 27-12-2015.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class DebugStatsStateTest {

    private DebugStatsState underTest;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Application app;
    @Mock
    private AppStateManager stateManager;
    @Mock
    private Node guiNode;
    private float tpf = 0f;
    @Captor
    private ArgumentCaptor<Node> debugNode;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BitmapFont guiFont;

    private BitmapText logMessageText;

    private BitmapText fpsText;
    @Mock
    private com.jme3.system.Timer timer;

    @Before
    public void setUp() {
        logMessageText = new BitmapText(guiFont);
        fpsText = new BitmapText(guiFont);
        underTest = new DebugStatsState(guiNode, fpsText, logMessageText);
        underTest.initialize(stateManager, app);
        verify(guiNode).attachChild(debugNode.capture());
        debugNode.getValue();
    }

    @Test
    public void testUpdate_notShown() {
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(0));
    }

    @Test
    public void testUpdate_shown() {
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, true, tpf);
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren(), hasItems(fpsText, logMessageText));
    }

    @Test
    public void testUpdate_fpsIsUpdatedCorrectly() {
        when(app.getTimer()).thenReturn(timer);
        when(timer.getTimePerFrame()).thenReturn(0.5f, 0.5f);
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, true, tpf);
        underTest.update(1);
        underTest.update(2);
        assertThat(fpsText.getText(), containsString("2"));
    }

    @Test
    public void testCleanup() {
        underTest.cleanup();
        assertThat(debugNode.getValue().getChildren().size(), is(0));
    }

    @Test
    public void testOnAction_toggleShown() {
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(0));
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, true, tpf);
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(2));
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, true, tpf);
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(0));
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, true, tpf);
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(2));
    }

    @Test
    public void testOnAction_notPressed() {
        assertThat(debugNode.getValue().getChildren().size(), is(2));
        underTest.onAction(DebugStatsState.TOGGLE_DEBUG_INFO, false, tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(2));
    }

    @Test
    public void testOnAction_unknownAction() {
        assertThat(debugNode.getValue().getChildren().size(), is(2));
        underTest.onAction("test", true, tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(2));
    }

}