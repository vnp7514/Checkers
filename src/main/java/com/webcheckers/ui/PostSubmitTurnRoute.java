package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    public PostSubmitTurnRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, Gson gson) {

        this.gson = gson;
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        LOG.fine("PostSubmitTurnRoute is invoked");

        final Message message;
        PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);


        if (playerServices == null) {
            LOG.fine("playerServices is null");
            response.redirect(WebServer.HOME_URL);
        } else {
            LOG.fine("playerServices is not null");

            Player player = playerServices.getPlayer();
            BoardView board = playerLobby.playerOfGame(player).getBoard();
            Color playerColor;
            if (player.equals(playerLobby.playerOfGame(player).getWhitePlayer())){
                playerColor = Color.WHITE;
            } else {
                playerColor = Color.RED;
            }
            if (board.newJumpExists(playerColor)) {
                LOG.fine("Jump Available!");
                message = Message.error("Jump Available!");
            } else {
                board.movePiece();
                LOG.fine("Submitted Move!");
                message = Message.info("Submitted Move!");
                playerLobby.playerOfGame(playerServices.getPlayer()).swapActiveColor();
            }
            String messageJSON = gson.toJson(message);
            return messageJSON;
        }
        return null;
    }
}
