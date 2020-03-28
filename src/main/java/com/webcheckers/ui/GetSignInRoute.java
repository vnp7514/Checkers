package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The {@code GET /signin} route handler.
 *
 * @author Van Pham <vnp7514@rit.edu>
 *
 */
public class GetSignInRoute implements Route {

    // Values used in the view-model map for rendering the signin view
    static final String VIEW_NAME = "signin.ftl";
    static final String TITLE_ATTR = "title";

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /signin} route handler
     *
     * @param templateEngine The {@Link TemplateEngine} used for rendering page HTML.
     */
    GetSignInRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        LOG.fine("GetSignInRoute is invoked");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        // Adding the title name
        vm.put(TITLE_ATTR, "Sign In Page");

        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        if (playerServices == null) {
            LOG.fine("PlayerServices is null");
            response.redirect(WebServer.HOME_URL);
            halt("PlayerServices is null");
            return null;
        } else {
            LOG.fine("PlayerServices is not null");
            if (playerServices.getPlayer() == null) {
                LOG.fine("This session has not signed in with a username");
                vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info("Please sign in"));
            } else {
                LOG.fine("This session has signed in!");
                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            }
        }
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
