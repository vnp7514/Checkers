package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.application.PlayerLobby;
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

    private final TemplateEngine templateEngine;

    // Key in the session attribute map for the player who started the session
    static final String PLAYERLOBBY_KEY = "playerLobby";

    // Values used in the view-model map for rendering the home view
    static final String TITLE_ATTR = "title";
    static final String MESSAGE_ATTR = "message";
    static final String USER_ATTR = "currentUser";
    static final String VIEW_NAME = "home.ftl";

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetHomeRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
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
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, "Welcome!");
        // display a user message in the Home page
        vm.put(MESSAGE_ATTR, WELCOME_MSG);

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        if(httpSession.attribute(PLAYERLOBBY_KEY)== null){
            final PlayerLobby playerLobby = new PlayerLobby();
            httpSession.attribute(PLAYERLOBBY_KEY, playerLobby);

            // render the View
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        } // TODO need a condition where the playerlobby is not null
        return null;
    }
}
