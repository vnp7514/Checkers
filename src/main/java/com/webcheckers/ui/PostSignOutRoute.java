package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostSignOutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    /**
     * Constructor for the code {@code POST /signout} route handler.
     *
     * @param playerLobby    the player lobby
     * @param templateEngine the template engine
     *
     * @throws  NullPointerException from Objects.requireNonNull() if it happens
     */
    public PostSignOutRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }


    /**
     * {@inheritDoc}
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        LOG.fine("GetSignOutRoute is invoked");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        // Adding the title name
        vm.put(GetHomeRoute.TITLE_ATTR, "Welcome!");

        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        if (playerServices == null) {
            LOG.fine("PlayerServices is null");
            response.redirect(WebServer.HOME_URL);
        } else {
            LOG.fine("PlayerServices is not null");
            if (playerServices.getPlayer() == null) {
                LOG.fine("This session has not signed in with a username");
                response.redirect(WebServer.HOME_URL);
            } else {
                LOG.fine("This session has signed in!");
                // remove the player from the lobby
                playerLobby.removePlayer(playerServices.getPlayer());
                // This session now has no username / signed out
                playerServices.deletePlayer();
                response.redirect(WebServer.HOME_URL);
            }
        }
        halt();
        return null;
    }
}
