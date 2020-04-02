package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Position;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostCheckTurnRoute implements Route {
    private final Gson gson;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());

    public PostCheckTurnRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson) {
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        Player player = playerServices.getPlayer();
        GameLobby gameLobby = playerLobby.playerOfGame(player);

        Player opponent;
        Color yourColor;
        if (gameLobby.getWhitePlayer() == player) {
            yourColor = Color.WHITE;
            opponent = gameLobby.getRedPlayer();
        }
        else {
            yourColor = Color.RED;
            opponent = gameLobby.getRedPlayer();
        }
        GameLobby other = playerLobby.playerOfGame(opponent);

        Message message;
        if (player == null) {
            LOG.fine("Player is null");
            return null;
        }
        if (gameLobby == null){
            LOG.fine("gameLobby is null");
            playerServices.storeMessage(Message.info("Your opponent has resigned"));
            return gson.toJson(Message.info("true"));
        }

        if (yourColor == gameLobby.getActiveColor() || yourColor == other.getActiveColor()) {
            LOG.fine("It's your turn!");
            message = Message.info("true");
        }
        else {
            LOG.fine("It is not your turn");
            message = Message.info("false");
        }
        return gson.toJson(message);
    }

}