package com.webcheckers.ui;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.webcheckers.appl.GameLobby;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostGameRoute implements Route {

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

        LOG.fine("Initialized");
        // Retrieve the HTTP session
        final Session httpSession = request.session();

        final PlayerServices playerServices =
                httpSession.attribute(GetHomeRoute.PLAYER_KEY);

        final String otherPlayer = request.queryParams(OTHER_PLAYER_PARAM);


        // The new player instance for the opponent
        final Player opponent = new Player(otherPlayer);


        if (playerServices != null) {
            if (playerServices.getPlayer() != null){
                LOG.fine("Retrieving the current Player from the PlayerServices");
                Player currentPlayer = playerServices.getPlayer();

                if (otherPlayer == null){
                    LOG.fine("No player was challenged");
                    playerServices.storeMessage(Message.error("No player was challenged"));
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }

                if(this.playerLobby.playerInGame(opponent)){
                    //If a player is already playing a game then print out an message and return to home
                    LOG.fine(String.format("This player %s is already in a game.",
                            opponent.getName()));
                    playerServices.storeMessage(Message.error(
                            String.format("%s is already in a game.",
                            opponent.getName())));
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                final GameLobby gameLobby = new GameLobby(currentPlayer, playerLobby.getNumGames());
                gameLobby.addPlayer(opponent);
                this.playerLobby.addGame(gameLobby);



                LOG.fine("Successfully create a gameLobby!");
                // GetGameRoute will handle the rendering and the game
                response.redirect(WebServer.GAME_URL);
                halt();
                return null;
            }
        }
        // if playerServices is null or the client has not signed in
        LOG.fine("the playerServices is null or the client has not signed in");
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }
}
