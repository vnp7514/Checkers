package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import com.webcheckers.util.ModeOptions;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;


public class PostResignRoute implements Route {

    private final String RESIGN_STR = " have resigned";

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
        Player currentPlayer = playerServices.getPlayer();
        GameLobby gameLobby = playerLobby.playerOfGame(currentPlayer);
        Message message;
        ModeOptions end;
        if ( gameLobby == null) {
            LOG.fine("GameLobby is null");
            message = Message.error("GameLobby is null");
            end = null;
        }
        else {
            LOG.fine("GameLobby is not null");
            LOG.fine("This player has resigned");
            playerLobby.removeGame(gameLobby);
             end = new ModeOptions(true,currentPlayer.getName() + RESIGN_STR);
        }

        LOG.fine(gson.toJson(end));
        return gson.toJson(end);
    }
}
