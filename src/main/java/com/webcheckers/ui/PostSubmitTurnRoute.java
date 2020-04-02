package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Position;
import spark.*;

import java.net.URLDecoder;
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

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);


        final BoardView board = playerServices.getGame();

        Move move = board.seeTopMove();

        Message message = null;
        
        Color playerColor = getColor(board, move);

        //Only for single pieces not king pieces
        if (board.isValidMove(move,board)){
            //If a single move was made
            if (move.getEnd().getRow() - move.getStart().getRow() == 1 &&
                    move.getEnd().getCell() - move.getStart().getCell() == 1 ||
                move.getEnd().getRow() - move.getStart().getRow() == 1 &&
                    move.getEnd().getCell() - move.getStart().getCell() == -1 ||
                move.getEnd().getRow() - move.getStart().getRow() == -1 &&
                    move.getEnd().getCell() - move.getStart().getCell() == 1 ||
                move.getEnd().getRow() - move.getStart().getRow() == -1 &&
                    move.getEnd().getCell() - move.getStart().getCell() == -1) {

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (board.viewPiece(i,j).getColor() == playerColor) {
                            if ( i+2< 8 && j-2 >= 0) {
                                Move t_r = new Move(new Position(i,j), new Position(i+2,j-2));
                                if (board.isValidJump(t_r, board)) {
                                    message = Message.error("You must jump");
                                    break;
                                }
                            }
                            if (i+2 < 8 && j+2 < 8) {
                                Move b_r = new Move(new Position(i,j), new Position(i+2, j+2));
                                if (board.isValidJump(b_r, board)) {
                                    message = Message.error("You must jump");
                                    break;
                                }
                            }
                            if (i-2 >= 0 && j-2 >=0) {
                                Move t_l = new Move(new Position(i,j), new Position(i-2, j-2));
                                if (board.isValidJump(t_l, board)) {
                                    message = Message.error("You must jump");
                                    break;
                                }
                            }
                            if (i-2 >= 0 && j+2 < 8) {
                                Move b_l = new Move(new Position(i,j), new Position(i-2,j+2));
                                if (board.isValidJump(b_l, board)) {
                                    message = Message.error("You must jump");
                                }
                            }
                        }
                    }
                }
            }
            //If the last move was a skip
            else if (move.getEnd().getRow() - move.getStart().getRow() == 2 &&
                    move.getEnd().getCell() - move.getStart().getCell() == 2 ||
                    move.getEnd().getRow() - move.getStart().getRow() == 2 &&
                            move.getEnd().getCell() - move.getStart().getCell() == -2 ||
                    move.getEnd().getRow() - move.getStart().getRow() == -2 &&
                            move.getEnd().getCell() - move.getStart().getCell() == 2 ||
                    move.getEnd().getRow() - move.getStart().getRow() == -2 &&
                            move.getEnd().getCell() - move.getStart().getCell() == -2) {
                if (board.viewPiece(move.getEnd().getRow(),move.getEnd().getCell()).getColor()
                        == playerColor) {
                    if ( move.getEnd().getRow()+2< 8 && move.getEnd().getCell()-2 >= 0) {
                        Move t_r = new Move(move.getEnd(),
                                new Position(move.getEnd().getRow()+2,move.getEnd().getCell()-2));
                        if (board.isValidJump(t_r, board)) {
                            message = Message.error("You have another skip");
                        }
                    }
                    if ( move.getEnd().getRow()+2< 8 && move.getEnd().getCell()+2 >= 0) {
                        Move t_r = new Move(move.getEnd(),
                                new Position(move.getEnd().getRow()+2,move.getEnd().getCell()+2));
                        if (board.isValidJump(t_r, board)) {
                            message = Message.error("You have another jump");
                        }
                    }
                    if ( move.getEnd().getRow()-2< 8 && move.getEnd().getCell()+2 >= 0) {
                        Move t_r = new Move(move.getEnd(),
                                new Position(move.getEnd().getRow()-2,move.getEnd().getCell()+2));
                        if (board.isValidJump(t_r, board)) {
                            message = Message.error("You have another jump");
                        }
                    }
                    if ( move.getEnd().getRow()-2< 8 && move.getEnd().getCell()-2 >= 0) {
                        Move t_r = new Move(move.getEnd(),
                                new Position(move.getEnd().getRow()-2,move.getEnd().getCell()-2));
                        if (board.isValidJump(t_r, board)) {
                            message = Message.error("You have another jump");
                        }
                    }
                }
            }
            else {
                message = Message.info("Move is valid");
            }
        } else {
            LOG.fine("move is invalid");
            message = Message.error("Move is invalid");
        }
        String messageJSON = gson.toJson(message);
        response.body(messageJSON);
        return null;
    }

    /**
     * Return
     * @param board
     * @param move
     * @return
     */
    public Color getColor(BoardView board, Move move){
        Position start = move.getStart();
        return board.viewPiece(start.getRow(),start.getCell()).getColor();
    }

}
