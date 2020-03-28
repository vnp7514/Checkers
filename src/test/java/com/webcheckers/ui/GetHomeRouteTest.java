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
public class GetHomeRouteTest {
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

    /**
     * Test that CuT shows the Home view when the session is brand new.
     */
    @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.WELCOME);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.ACTIVE_USERS_ATTR,
                String.format("There are %d users online.", playerLobby.lobbySize()));
        testHelper.assertViewModelAttribute(GetHomeRoute.USER_ATTR, null);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_ATTR, null);


        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYER_KEY), any(PlayerServices.class));
        verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
                any(SessionTimeoutWatchdog.class));
    }

    /**
     * Test that CuT shows the Home view when the session is brand new
     *     and there are some people who have signed in.
     */
    @Test
    public void new_session_with_signin() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        playerLobby.addPlayer(p1);
        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.WELCOME);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.ACTIVE_USERS_ATTR,
                String.format("There are %d users online.", 1));
        testHelper.assertViewModelAttribute(GetHomeRoute.USER_ATTR, null);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_ATTR, null);


        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYER_KEY), any(PlayerServices.class));
        verify(session).attribute(eq(GetHomeRoute.TIMEOUT_SESSION_KEY),
                any(SessionTimeoutWatchdog.class));
    }
    /**
     * Test the Home View when the player has just signed in
     */
    @Test
    public void sign_in_session(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Mocking a signed in player
        playerLobby.addPlayer(p1);
        final PlayerServices playerService = playerLobby.newPlayerServices();
        playerService.insertPlayer(p1);
        session.attribute(GetHomeRoute.PLAYER_KEY, playerService);
        session.attribute(GetHomeRoute.TIMEOUT_SESSION_KEY, new SessionTimeoutWatchdog(playerService));
        session.maxInactiveInterval(GetHomeRoute.SESSION_TIMEOUT_PERIOD);
        when(session.attribute(GetHomeRoute.PLAYER_KEY)).thenReturn(playerService);

        // Invoke the test
        CuT.handle(request, response);


        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.WELCOME);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.ACTIVE_USERS_ATTR, "");
        testHelper.assertViewModelAttribute(GetHomeRoute.USER_ATTR, playerService.getPlayer());
        List<String> players = playerLobby.availablePlayers();
        players.remove(playerService.getPlayer().getName());
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_LIST_ATTR, players);

        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }
}
