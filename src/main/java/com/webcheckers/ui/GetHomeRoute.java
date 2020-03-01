package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.Checkers.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.PlayerServices;
import spark.*;

import com.webcheckers.util.Message;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

    // The length of the session timeout in seconds
    static final int SESSION_TIMEOUT_PERIOD = 120;

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    // Key in the session attribute map for the player who started the session
    static final String PLAYER_KEY = "player";
    static final String TIMEOUT_SESSION_KEY = "timeoutWatchdog";

    // Values used in the view-model map for rendering the home view
    static final String TITLE_ATTR = "title";
    static final String MESSAGE_ATTR = "message";
    static final String USER_ATTR = "currentUser";
    static final String VIEW_NAME = "home.ftl";
    static final String ACTIVE_USERS = "otherUsers";

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetHomeRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        //
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");
        // View-Model
        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, "Welcome!");
        // display a user message in the Home page
        vm.put(MESSAGE_ATTR, WELCOME_MSG);

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        if (playerLobby.lobbySize() <= 1) {
            vm.put(ACTIVE_USERS, "There are no other players available to play at this time.");
        }

        // if this is a brand new browser session or a session that timed out
        if (httpSession.attribute(PLAYER_KEY) == null){
            LOG.fine("A brand new session is created!");
            // Get the object that will provide client-specific services for this
            final PlayerServices playerService = playerLobby.newPlayerServices();
            httpSession.attribute(PLAYER_KEY, playerService);

            // setup session timeout. The valueUnbound() method in the SessionTimeoutWatchdog will
            // be called when the session is invalidated. The next invocation of this route will
            // have a new Session object with no attributes.
            httpSession.attribute(TIMEOUT_SESSION_KEY, new SessionTimeoutWatchdog(playerService));
            httpSession.maxInactiveInterval(SESSION_TIMEOUT_PERIOD);


        } else {
            // This user has already been here
            PlayerServices playerServices = httpSession.attribute(PLAYER_KEY);

            if (playerServices.getPlayer() != null){
                // if the user has signed in with a username
                LOG.fine("A signed in player!");
                vm.put(USER_ATTR, playerServices.getPlayer());

            } else {
                LOG.fine("A player without a username");
                if (playerLobby.lobbySize() >= 1) {
                    vm.put(ACTIVE_USERS,
                            String.format("There are %d other users online.", playerLobby.lobbySize()));
                }
            }
        }
        //render the Home Page
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
