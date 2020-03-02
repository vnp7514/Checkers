package com.webcheckers.ui;

import com.webcheckers.Checkers.BoardView;
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

        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        if (playerServices != null) {


            vm.put(ACTIVE_COLOR, Color.WHITE);
            vm.put(TITLE, "Checkers game!");
            vm.put(VIEW, ViewMode.PLAY);
            //need to put player instances in all of these below

            vm.put(CURRENT_USER, playerServices.getPlayer());
            vm.put(RED_PLAYER, playerServices.getPlayer());
            vm.put(WHITE_PLAYER, playerServices.getPlayer());
            vm.put(GAME_BOARD, this.boardView);
            vm.put(MODE, null );
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
        else {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
