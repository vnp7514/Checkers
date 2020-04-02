package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostResignGameRoute implements Route {

    private final Gson gson;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    // For logging (printing out messages for server)
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    public PostResignGameRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson) {
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }

    public Object handle(Request request, Response response){
        LOG.fine("PostResignGameRoute is called");
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        // The game that the player is currently in
        final GameLobby gameLobby = playerLobby.playerOfGame(playerServices.getPlayer());

        if ( gameLobby != null){
            LOG.fine("GameLobby is not null");
            playerLobby.removeGame(gameLobby);
            response.redirect(WebServer.HOME_URL);
            halt();
            return Message.info(playerServices.getPlayer().getName() + "has resigned!");
        } else {
            LOG.fine("gameLobby is null");
            return Message.error("Unable to resign the game");
        }
    }
}
