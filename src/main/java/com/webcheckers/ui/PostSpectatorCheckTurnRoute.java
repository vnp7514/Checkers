package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostSpectatorCheckTurnRoute implements Route {

    private static final String SPEC_CHECK_TURN_URL = "/spectator/checkTurn";

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.PLAYER_KEY);

    public PostSpectatorCheckTurnRoute(PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        this.gson = gson;
    }



    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final String temp = request.body();
        String[] temp1 = temp.split("[=]+");
        int gameID = Integer.parseInt(temp1[1]);
        Message message;

        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                //uses gameID to access the GameLobby the user is spectating
                GameLobby gameLobby = playerLobby.getGame(gameID);
                if (gameLobby != null) {
                    if (playerLobby.getBoard(gameLobby).winCondition() || gameLobby.specColor()) {
                        message = Message.info("true");
                    }
                    else {
                        message = Message.info("false");
                    }
                    String messageJSON = gson.toJson(message);
                    return messageJSON;
                } else {
                    LOG.fine("gameLobby is null so the player is not watching a game");

                }
            } else {
                LOG.fine("the client has not signed in");
            }
        } else {
            LOG.fine("the playerServices is null");
        }
        response.redirect(WebServer.HOME_URL);
        halt();

        return null;
    }
}
