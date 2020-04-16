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

    private final String RESIGN_STR = " has resigned";

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
        if ( gameLobby == null) {
            LOG.fine("GameLobby is null");
            message = Message.error("GameLobby is null");
        } else {
            LOG.fine("GameLobby is not null");
            LOG.fine(currentPlayer.getName() + " has resigned");
            message = Message.info(currentPlayer.getName() + RESIGN_STR);
            gameLobby.resign(currentPlayer);
            /*// Get the opposing player
            Player opposingPlayer;
            if (gameLobby.getWhitePlayer().equals(currentPlayer)) {
                opposingPlayer = gameLobby.getRedPlayer();
            }
            else {
                opposingPlayer = gameLobby.getWhitePlayer();
            }
            // Swap the active color so the opposing player's color is the active color
            gameLobby.swapActiveColor();
            // Give the opposing player a copy of the game lobby
            opposingPlayer.setGameLobbyCopy(gameLobby);
            // Give the opposing player the game over message
            opposingPlayer.setGameOverMessage(message.getText());
            playerLobby.removeGame(gameLobby);
            */
        }
        return gson.toJson(message);
    }
}
