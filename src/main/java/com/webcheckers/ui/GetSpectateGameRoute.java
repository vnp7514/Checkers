package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.webcheckers.model.ViewMode;
import spark.*;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSpectateGameRoute implements Route {

    private final String TITLE = "title";
    private final String VIEW_NAME = "game.ftl";
    private final String GAME_ID = "gameID";
    private final String CURRENT_USER = "currentUser";
    private final String GAME_BOARD = "board";
    private final String VIEW = "viewMode";
    private final String MODE = "modeOptionsAsJSON";
    private final String RED_PLAYER = "redPlayer";
    private final String WHITE_PLAYER = "whitePlayer";
    private final String ACTIVE_COLOR = "activeColor";

    private final String GAME = "game";

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    public GetSpectateGameRoute(PlayerLobby playerLobby, TemplateEngine templateEngine, Gson gson) {
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        this.gson = gson;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Map<String, Object> vm = new HashMap<>();

        final Map<String, Object> modeOptions = new HashMap<>(2);

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final String temp = request.queryParams(GAME);
        int gameID = Integer.parseInt(temp);

        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                //uses gameID to access the GameLobby the user is spectating
                GameLobby gameLobby = playerLobby.getGame(gameID);
                if (gameLobby != null && !playerLobby.getBoard(gameLobby).winCondition()) {
                    if (!gameLobby.containSpectator(currentPlayer)) {
                        gameLobby.addSpectator(currentPlayer);
                    }
                    //populates the view model with info contained in gameLobby
                    vm.put(GAME_ID, gameID);
                    vm.put(CURRENT_USER, currentPlayer);
                    vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
                    vm.put(TITLE, "Checkers game!");
                    vm.put(VIEW, ViewMode.SPECTATOR);
                    //need to put player instances in all of these below
                    vm.put(RED_PLAYER, gameLobby.getRedPlayer());
                    vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
                    vm.put(GAME_BOARD, gameLobby.getBoard());
                    vm.put(MODE, null);

                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                } else {
                    LOG.fine("gameLobby is null so the player is not watching a game");
                    modeOptions.put("isGameOver", true);
                    modeOptions.put("gameOverMessage", request.body());
                    vm.put(MODE, gson.toJson(modeOptions));
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
