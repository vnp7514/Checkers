package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final BoardView boardView;
    private final Gson gson;

    public PostSubmitTurnRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, Gson gson) {

        this.gson = gson;
        this.boardView = new BoardView();
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

            BoardView board = playerServices.getGame();
            if (board.newMoveExists(board.seeTopMove(), board)) {
                LOG.fine("Jump Available!");
                message = Message.error("Jump Available!");
            } else {
                board.movePiece(board.seeTopMove(), board);
                LOG.fine("Submitted Move!");
                message = Message.info("Submitted Move!");
                board.removeAllMoves();
            }
            playerLobby.playerOfGame(playerServices.getPlayer()).swapActiveColor();
            String messageJSON = gson.toJson(message);
            return messageJSON;
        }
        return null;
    }
}
