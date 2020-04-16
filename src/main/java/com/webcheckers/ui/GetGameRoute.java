package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import spark.*;

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

        final Map<String, Object> vm = new HashMap<>();

        final Map<String, Object> modeOptions = new HashMap<>(2);

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final BoardView board;

        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                GameLobby gameLobby = playerLobby.playerOfGame(currentPlayer);
                if (gameLobby != null && !playerLobby.getBoard(gameLobby).winCondition()) {
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
