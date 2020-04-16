package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
import com.webcheckers.model.Player;
import com.webcheckers.model.ViewMode;
import com.webcheckers.util.Message;
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

    private final String WIN_MSG = " has captured all the pieces";
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

        final Map<String, Object> vm;

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
                if (gameLobby != null) {
                    if (!gameLobby.containSpectator(currentPlayer)) {
                        gameLobby.addSpectator(currentPlayer);
                    }
                    if (gameLobby.isRunning()){
                        LOG.fine("Noone has resigned or lost the game");
                        vm = noResignation(gameLobby, playerServices);
                    } else {
                        if (gameLobby.getQuitter() != null){
                            LOG.fine("Someone has resigned the game");
                            vm = resignation(gameLobby, playerServices);
                        } else {
                            LOG.fine("Someone has lost");
                            vm = noResignation(gameLobby, playerServices);
                        }
                    }
                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                } else {
                    LOG.fine("gameLobby is null so the player is not watching a game");
                    playerServices.storeMessage(Message.info("GameLobby "
                            + gameID + " is not available anymore"));
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


    /**
     * Return the ViewModel for the case where both players are playing the game and noone
     *  has resigned
     * @param gameLobby the game lobby that 2 players are in
     * @param playerServices the current player session
     * @return the ViewModel used for rendering the page
     */
    private Map<String, Object> noResignation(GameLobby gameLobby, PlayerServices playerServices){
        BoardView boardView = gameLobby.getBoard();
        Player currentPlayer = playerServices.getPlayer();
        boolean isGameOver;
        String gameOverMessage;
        Message message = null;

        if (boardView.winCondition()){
            isGameOver = true;
            if(gameLobby.getActiveColor() == Color.WHITE){
                LOG.fine("Game is over because white has no pieces left");
                gameOverMessage = gameLobby.getRedPlayer().getName() + WIN_MSG;
            } else {
                LOG.fine("game is over because red has no pieces left");
                gameOverMessage = gameLobby.getWhitePlayer().getName() + WIN_MSG;
            }
        }
        else if (gameLobby.getActiveColor() == Color.WHITE &&
                !boardView.newMoveExists(Color.WHITE)){
            LOG.fine("Game is over because white player cannot move");
            isGameOver = true;
            message = Message.info(gameLobby.getRedPlayer().getName() + " won.");
            gameOverMessage = gameLobby.getWhitePlayer().getName()
                    + " cannot move anymore.";
        }
        else if (gameLobby.getActiveColor() == Color.RED &&
                !boardView.newMoveExists(Color.RED)){
            LOG.fine("Game is over because Red player cannot move");
            isGameOver = true;
            message = Message.info(gameLobby.getWhitePlayer().getName()+ " won.");
            gameOverMessage = gameLobby.getRedPlayer().getName()
                    + " cannot move anymore.";
        }
        else {
            LOG.fine("Game is still going");
            isGameOver = false;
            gameOverMessage = "";
        }

        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        playerServices.removeMessage();
        vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
        vm.put(TITLE, "Checkers game!");
        vm.put(VIEW, ViewMode.SPECTATOR);
        //need to put player instances in all of these below
        vm.put(CURRENT_USER, currentPlayer);
        vm.put(RED_PLAYER, gameLobby.getRedPlayer());
        vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
        vm.put(GAME_ID, gameLobby.getGameID());
        vm.put(GAME_BOARD, boardView);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", isGameOver);
        modeOptions.put("gameOverMessage", gameOverMessage);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        return vm;
    }

    /**
     * Someone has surrender the game
     * @param gameLobby the game lobby that 2 players were in
     * @param playerServices the current player session
     * @return the ViewModel used for rendering the page of the person who did not resign
     */
    private Map<String, Object> resignation(GameLobby gameLobby, PlayerServices playerServices){
        Player current = playerServices.getPlayer();
        Message message;
        BoardView boardView;
        // Someone quits.

        message = Message.info(gameLobby.getQuitter().getName() +
                " has resigned!");
        boardView = gameLobby.getBoard();
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
        vm.put(TITLE, "Checkers game!");
        vm.put(VIEW, ViewMode.SPECTATOR);
        //need to put player instances in all of these below
        vm.put(CURRENT_USER, current);
        vm.put(RED_PLAYER, gameLobby.getRedPlayer());
        vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
        vm.put(GAME_ID, gameLobby.getGameID());
        vm.put(GAME_BOARD, boardView);
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", null);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        return vm;
    }

}
