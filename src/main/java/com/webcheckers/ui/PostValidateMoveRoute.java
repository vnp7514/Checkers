package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Position;
import spark.*;

import java.net.URLDecoder;
import java.util.Objects;
import java.util.logging.Logger;

public class PostValidateMoveRoute implements Route {
    private final Gson gson;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    public PostValidateMoveRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }


    public Object handle(Request request, Response response){
        // Get the string from the body
        String JSON = request.body();
        try {
            JSON = URLDecoder.decode(JSON, "UTF-8");
        } catch (Exception e){}
        JSON = JSON.substring(11);
        LOG.fine(JSON + " returned from the request.body()");
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final BoardView board = playerServices.getGame();

        final Move move = gson.fromJson(JSON, Move.class);

        final Message message;
        if (isValidMove(board, move)){
            board.addMove(move);
            LOG.fine("move is valid");
            message = Message.info("Move is valid.");

        } else {
            LOG.fine("move is invalid");

            message = Message.error("Move is invalid");
        }
        String messageJSON = gson.toJson(message);
        return messageJSON;
    }


    /**
     * Return true if the move is valid
     * @param board the board
     * @param move the move
     * @return true if the above conditions are true
     */
    public boolean isValidMove(BoardView board, Move move){
        Position end = move.getEnd();
        return board.isValidMove(move);
    }
}
