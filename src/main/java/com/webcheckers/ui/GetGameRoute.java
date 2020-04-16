package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetGameRoute implements Route {

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

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;
    private final Gson gson;

    //private final BoardView boardView;

    /**
     * Constructor
     * @param playerLobby
     * @param templateEngine
     */
    public GetGameRoute (final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson) {
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        //this.boardView = new BoardView();
        this.templateEngine = templateEngine;
        this.gson = Objects.requireNonNull(gson, "gson must not be null");

        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Map<String, Object> vm;

        final Map<String, Object> modeOptions = new HashMap<>(2);

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final BoardView board;

        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                if (playerLobby.resignerOfGame(currentPlayer) != null){
                    playerServices.storeMessage(Message.info("Successfully resigned the game"));
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                GameLobby gameLobby = playerLobby.playerOfGame(currentPlayer);
                if (gameLobby == null){
                    playerServices.storeMessage(Message.error("The game you were in is no longer active"));
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                if (gameLobby.isRunning()){
                    LOG.fine("gameLobby is active/ Noone has resigned/lost the game");
                    vm = noResignation(gameLobby, playerServices);
                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

                } else {
                    if (gameLobby.getQuitter() != null) {
                        LOG.fine("gameLobby is not active/ Someone has resigned/Game is over");
                        vm = resignation(gameLobby, playerServices);
                    } else {
                        LOG.fine("gameLobby is not active/ Someone has lost");
                        vm = noResignation(gameLobby, playerServices);
                    }
                    return templateEngine.render (new ModelAndView(vm, VIEW_NAME));
                }
                /**if (gameLobby != null && !playerLobby.getBoard(gameLobby).winCondition()) {
                    if (gameLobby.getWhitePlayer().equals(currentPlayer)) { // the current player is white
                        board = playerLobby.getFlippedBoard(gameLobby);
                        LOG.fine("Flipping Board!");
                    } else { // the current player is red
                        board = gameLobby.getBoard();
                        LOG.fine("Not Flipping Board!");
                    }

                    vm.put(GetHomeRoute.MESSAGE_ATTR,playerServices.getMessage());
                    playerServices.removeMessage();
                    vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
                    vm.put(TITLE, "Checkers game!");
                    vm.put(VIEW, ViewMode.PLAY);
                    //need to put player instances in all of these below
                    vm.put(CURRENT_USER, currentPlayer);
                    vm.put(RED_PLAYER, gameLobby.getRedPlayer());
                    vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
                    vm.put(GAME_ID, gameLobby.getGameID());
                    vm.put(GAME_BOARD, board);
                    vm.put(MODE, null);

                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                }

                else {
                    if (gameLobby != null) {
                        LOG.fine("The game has ended");
                        Color yourColor;
                        if (gameLobby.getWhitePlayer().equals(currentPlayer)) {
                            yourColor = Color.WHITE;
                        }
                        else {
                            yourColor = Color.RED;
                        }
                        Player opposingPlayer;
                        if (gameLobby.getWhitePlayer().equals(currentPlayer)) {
                            opposingPlayer = gameLobby.getRedPlayer();
                        }
                        else {
                            opposingPlayer = gameLobby.getWhitePlayer();
                        }
                        String gameOverMessage;
                        if (yourColor == gameLobby.getActiveColor()) {
                            gameOverMessage = opposingPlayer.getName() + WIN_MSG;

                        }
                        else {
                            gameOverMessage = currentPlayer.getName() + WIN_MSG;
                        }
                        LOG.fine(gameOverMessage);
                        vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
                        vm.put(TITLE, "Game Over!");
                        vm.put(VIEW, ViewMode.PLAY);
                        vm.put(CURRENT_USER, currentPlayer);
                        vm.put(RED_PLAYER, gameLobby.getRedPlayer());
                        vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
                        vm.put(GAME_ID, gameLobby.getGameID());
                        vm.put(GAME_BOARD, gameLobby.getBoard());
                        modeOptions.put("isGameOver", true);
                        modeOptions.put("gameOverMessage", gameOverMessage);
                        vm.put(MODE, gson.toJson(modeOptions));
                        // TODO
                        // Clicking exit button does not redirect to home
                        // Removing game lobby or removing current player does not work
                        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                    }
                    else {
                        LOG.fine("gameLobby is null so the player is not in a game");
                        GameLobby gameOver = currentPlayer.getGameLobbyCopy();
                        vm.put(ACTIVE_COLOR, gameOver.getActiveColor());
                        vm.put(TITLE, "Game Over!");
                        vm.put(VIEW, ViewMode.PLAY);
                        vm.put(CURRENT_USER, currentPlayer);
                        vm.put(RED_PLAYER, gameOver.getRedPlayer());
                        vm.put(WHITE_PLAYER, gameOver.getWhitePlayer());
                        vm.put(GAME_ID, gameOver.getGameID());
                        vm.put(GAME_BOARD, gameOver.getBoard());
                        modeOptions.put("isGameOver", true);
                        modeOptions.put("gameOverMessage", currentPlayer.getGameOverMessage());
                        vm.put(MODE, gson.toJson(modeOptions));
                        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                    }
                }
                 */
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
            gameLobby.end();
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
            Color currentColor;
            if (gameLobby.getWhitePlayer().equals(currentPlayer)){
                currentColor = Color.WHITE;
            } else {
                currentColor = Color.RED;
            }
            if (currentColor == gameLobby.getActiveColor()) {
                // if the current player cannot move
                LOG.fine("Game is over because white player cannot move");
                isGameOver = true;
                gameLobby.end();
                message = Message.info(gameLobby.getRedPlayer().getName() + " won.");
                gameOverMessage = gameLobby.getWhitePlayer().getName()
                        + " cannot move anymore.";
            } else {
                // Not this player turn so of course he cant move
                if (gameLobby.isRunning()) {
                    isGameOver = false;
                    gameOverMessage = "";
                } else {
                    isGameOver = true;
                    gameLobby.end();
                    message = Message.info(gameLobby.getRedPlayer().getName() + " won.");
                    gameOverMessage = gameLobby.getWhitePlayer().getName()
                            + " cannot move anymore.";
                }
            }
        }
        else if (gameLobby.getActiveColor() == Color.RED &&
                !boardView.newMoveExists(Color.RED)){
            Color currentColor;
            if (gameLobby.getWhitePlayer().equals(currentPlayer)){
                currentColor = Color.WHITE;
            } else {
                currentColor = Color.RED;
            }
            if (currentColor == gameLobby.getActiveColor()) {
                LOG.fine("Game is over because Red player cannot move");
                isGameOver = true;
                gameLobby.end();
                message = Message.info(gameLobby.getWhitePlayer().getName() + " won.");
                gameOverMessage = gameLobby.getRedPlayer().getName()
                        + " cannot move anymore.";
            } else {
                if (gameLobby.isRunning()) {
                    isGameOver = false;
                    gameOverMessage = "";
                } else {
                    isGameOver = true;
                    gameLobby.end();
                    message = Message.info(gameLobby.getWhitePlayer().getName() + " won.");
                    gameOverMessage = gameLobby.getRedPlayer().getName()
                            + " cannot move anymore.";
                }
            }
        }
        else {
            LOG.fine("Game is still going");
            isGameOver = false;
            gameOverMessage = "";
        }

        // Giving to each player their unique view of the board
        if (gameLobby.getWhitePlayer().equals(currentPlayer)){
            // The current player is white
            boardView = gameLobby.getBoardFlipped();

        } else {
            // The current player is red
            boardView = gameLobby.getBoard();
        }

        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        playerServices.removeMessage();
        vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
        vm.put(TITLE, "Checkers game!");
        vm.put(VIEW, ViewMode.PLAY);
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
        if (current.equals(gameLobby.getRedPlayer())) {
            LOG.fine(current.getName() + "is a red player who got resigned on");
            message = Message.info(gameLobby.getWhitePlayer().getName() +
                    " has resigned!");
            boardView = gameLobby.getBoardFlipped();

        } else {
            LOG.fine(current.getName() + " is a white player who got resigned on");
            message = Message.info(gameLobby.getRedPlayer().getName() +
                    " has resigned!");
            boardView = gameLobby.getBoard();
        }
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        vm.put(ACTIVE_COLOR, gameLobby.getActiveColor());
        vm.put(TITLE, "Checkers game!");
        vm.put(VIEW, ViewMode.PLAY);
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
