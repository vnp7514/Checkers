package com.webcheckers.ui;

import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * The unit test suite for the {@Link GetHomeRoute} component.
 * @author Van Pham
 */
@Tag("UI-tier")
public class GetGameRouteTest {
    /**
     * The component under test (CuT)
     *
     * This is a stateless component so we only need one.
     */
    private GetHomeRoute CuT;

    // Hopefully friendly object TODO
    private PlayerLobby playerLobby;

    // Friendly objects
    Player p1 = new Player("Player 1");
    Player p2 = new Player("Player 2");
    Player p3 = new Player("Player 3");
    Player p4 = new Player("Player 4");

    // mock objects
    private Request request;
    private TemplateEngine templateEngine;
    private Session session;
    private Response response;

    /**
     * Setup new mock objects for each test
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        when(request.session()).thenReturn(session);


        // create a unique CuT for each test
        // the PlayerLobby is friendly but the engine mock will need configuration
        playerLobby = new PlayerLobby();
        CuT = new GetHomeRoute(playerLobby, templateEngine);
    }

    @Test
    public void noResignationTest() {

    }
}
