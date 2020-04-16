package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
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


    @Override
    public Object handle(Request request, Response response){
        // Get the string from the body
        String JSON = request.body();
        try {
            JSON = URLDecoder.decode(JSON, "UTF-8");
        } catch (Exception e){}
        JSON = JSON.substring(20);
        LOG.fine(JSON + " returned from the request.body()");
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        //final BoardView board = playerServices.getGame();

        GameLobby gameLobby = playerLobby.playerOfGame(playerServices.getPlayer());
        if (gameLobby == null){
            LOG.fine("gameLobby is null");
            return null;
        }
        BoardView board = gameLobby.getBoard();
        System.out.println(JSON);
        final Move move = gson.fromJson(JSON, Move.class);

        final Message message;
        final Color playerColor;
        if (gameLobby.getWhitePlayer().equals(playerServices.getPlayer())){
            playerColor = Color.WHITE;
        } else {
            playerColor = Color.RED;
        }
        if (isValidMove(board, move, playerColor)){
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
     * @param playerColor the color of the player
     * @return true if the above conditions are true
     */
    public boolean isValidMove(BoardView board, Move move, Color playerColor){
        if (board.newJumpExists(playerColor)){
            return board.isValidJump(move);
        }
        return board.isValidMove(move);
    }
}
