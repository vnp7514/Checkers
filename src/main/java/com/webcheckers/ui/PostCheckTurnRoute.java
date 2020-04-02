package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.util.Message;
import com.webcheckers.util.Move;
import com.webcheckers.util.Position;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostCheckTurnRoute implements Route {
    private final Gson gson;
    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    public PostCheckTurnRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine, final Gson gson) {
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.gson = Objects.requireNonNull(gson, "gson must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    }


    public Object handle(Request request, Response response) {
        // Get the string from the body
        return null;
    }

}