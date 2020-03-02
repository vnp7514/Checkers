package com.webcheckers.ui;

import com.webcheckers.Checkers.BoardView;
import com.webcheckers.Checkers.Player;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.PlayerServices;
import com.webcheckers.model.Color;
import com.webcheckers.model.ViewMode;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostGameRoute implements Route {
    private final String TITLE = "title";
    private final String VIEW_NAME = "game.ftl";
    private final String CURRENT_USER = "currentUser";
    private final String GAME_BOARD = "board";
    private final String VIEW = "viewMode";
    private final String MODE = "modeOptions";
    private final String RED_PLAYER = "redPlayer";
    private final String WHITE_PLAYER = "whitePlayer";
    private final String ACTIVE_COLOR = "activeColor";

    private final String OTHER_PLAYER_PARAM = "otherPlayer";

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private final BoardView boardView;

    /**
     * Constructor
     * @param playerLobby
     * @param templateEngine
     */
    public PostGameRoute (final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.boardView = new BoardView();
        this.templateEngine = templateEngine;
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();

        LOG.fine("Initialized");
        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final String otherPlayer = request.queryParams(OTHER_PLAYER_PARAM);
        if (otherPlayer == null){
            LOG.fine("No player was challenged");
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }

        // The new player instance for the opponent
        final Player opponent = new Player(otherPlayer);


        if (playerServices != null) {
            if (playerServices.getPlayer() != null){
                LOG.fine("Retrieving the current Player from the PlayerServices");
                Player currentPlayer = playerServices.getPlayer();
                if(this.playerLobby.playerInGame(currentPlayer)) {
                    LOG.fine("This player " + currentPlayer.getName() +
                            " is already in the game");
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                if(this.playerLobby.playerInGame(opponent)){
                    LOG.fine("This opponent " + opponent.getName() +
                            " is already in the game");
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                final GameLobby gameLobby = new GameLobby(currentPlayer);
                gameLobby.addPlayer(opponent);
                this.playerLobby.addGame(gameLobby);

                vm.put(ACTIVE_COLOR, Color.WHITE);
                vm.put(TITLE, "Checkers game!");
                vm.put(VIEW, ViewMode.PLAY);
                //need to put player instances in all of these below

                vm.put(CURRENT_USER, currentPlayer);
                vm.put(RED_PLAYER, currentPlayer);
                vm.put(WHITE_PLAYER, opponent);
                vm.put(GAME_BOARD, this.boardView);
                vm.put(MODE, null );
                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
            }
        }
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }
}
