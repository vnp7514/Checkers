package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostBackupMoveRoute implements Route {
    private final Gson gson;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());

    public PostBackupMoveRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }

    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);
        final BoardView boardView = playerServices.getGame();
        Move move = boardView.removeMove();
        final Message message;
        if (move == null){
            message = Message.error("Backup error!");
        } else {
            String string = "Move back the piece from row:" + move.getEnd().getCell() +
                    " col:" + move.getEnd().getRow() + " to row:" + move.getStart().getCell()
                    + " col:" + move.getStart().getRow();
            message = Message.info(string);
        }
        return gson.toJson(message);
    }
}
