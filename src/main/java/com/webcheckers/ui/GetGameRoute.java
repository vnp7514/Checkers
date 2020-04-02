package com.webcheckers.ui;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Color;
import com.webcheckers.model.ViewMode;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetGameRoute implements Route {

    private final String TITLE = "title";
    private final String VIEW_NAME = "game.ftl";
    private final String CURRENT_USER = "currentUser";
    private final String GAME_BOARD = "board";
    private final String VIEW = "viewMode";
    private final String MODE = "modeOptionsAsJSON";
    private final String RED_PLAYER = "redPlayer";
    private final String WHITE_PLAYER = "whitePlayer";
    private final String ACTIVE_COLOR = "activeColor";

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    //private final BoardView boardView;

    /**
     * Constructor
     * @param playerLobby
     * @param templateEngine
     */
    public GetGameRoute (final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        //this.boardView = new BoardView();
        this.templateEngine = templateEngine;
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {

        final Map<String, Object> vm = new HashMap<>();

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final BoardView board;

        final BoardView PSboard;

        if (playerServices != null) {
            Player currentPlayer = playerServices.getPlayer();
            if (currentPlayer != null) {
                GameLobby gameLobby = playerLobby.playerOfGame(currentPlayer);
                if (gameLobby != null) {
                    if (gameLobby.getWhitePlayer().equals(currentPlayer)) { // the current player is white
                        PSboard = gameLobby.getBoard();
                        board = playerLobby.getFlippedBoard(gameLobby);
                        LOG.fine("Flipping Board!");
                    } else { // the current player is red
                        PSboard = gameLobby.getBoard();
                        board = gameLobby.getBoard();
                        LOG.fine("Not Flipping Board!");
                    }
                    playerServices.addBoard(PSboard);

                    vm.put(ACTIVE_COLOR, Color.WHITE);
                    vm.put(TITLE, "Checkers game!");
                    vm.put(VIEW, ViewMode.PLAY);
                    //need to put player instances in all of these below
                    vm.put(CURRENT_USER, currentPlayer);
                    vm.put(RED_PLAYER, gameLobby.getRedPlayer());
                    vm.put(WHITE_PLAYER, gameLobby.getWhitePlayer());
                    vm.put(GAME_BOARD, board);
                    vm.put(MODE, null);


                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                } else {
                    LOG.fine("gameLobby is null so the player is not in a game");
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
