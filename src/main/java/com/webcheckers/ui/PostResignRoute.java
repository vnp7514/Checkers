package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;


public class PostResignRoute implements Route {

    private final String RESIGN_STR = "You have resigned";

    private static final Logger LOG = Logger.getLogger(PostResignRoute.class.getName());
    private final Gson gson;
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    public PostResignRoute(PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        LOG.fine("PostResignRoute Initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.fine("PostResignRoute is called!");
        final Session httpSession = request.session();
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
                LOG.fine("This player has resigned");
                Player currentPlayer = playerServices.getPlayer();
                playerLobby.removeGame(playerLobby.playerOfGame(currentPlayer));
            }
        }
        Message message = Message.resign(RESIGN_STR);
        return gson.toJson(message);
    }
}
