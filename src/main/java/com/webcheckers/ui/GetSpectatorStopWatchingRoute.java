package com.webcheckers.ui;

import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSpectatorStopWatchingRoute implements Route {

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.PLAYER_KEY);

    public GetSpectatorStopWatchingRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Session httpSession = request.session();

        PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final String temp = request.queryParams("gameID");
        int gameID = Integer.parseInt(temp);



        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                //uses gameID to access the GameLobby the user is spectating
                GameLobby gameLobby = playerLobby.getGame(gameID);
                if (gameLobby != null) {
                    gameLobby.removeSpectator(currentPlayer);

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
