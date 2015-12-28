package nl.civcraft.core.debug;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;

/**
 * Created by Bob on 27-12-2015.
 * <p>
 * This is probably not worth documenting
 */
@RunWith(MockitoJUnitRunner.class)
public class DebugStatsStateTest {

    @InjectMocks
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

    @Before
    public void setUp() {
        underTest.initialize(stateManager, app);
        verify(guiNode).attachChild(debugNode.capture());
        debugNode.getValue();
    }

    @Test
    public void testUpdate_notShown() {
        underTest.update(tpf);
        assertThat(debugNode.getValue().getChildren().size(), is(0));
    }

}