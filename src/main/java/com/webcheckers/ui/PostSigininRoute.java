package com.webcheckers.ui;

import com.webcheckers.Checkers.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSigininRoute implements Route {

    private final TemplateEngine templateEngine;
    static final String NAME_PARAM = "username";

    /**
     * The constructor for the code {@code POST /signin} route handler.
     *
     * @param templateEngine the template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *     when the {@code templateEngine} is null
     */
    PostSigininRoute(TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // View-model
        final Map<String, Object> vm = new HashMap<>();
        // TODO need a condition statement here in case of Lobby is full or null
        final Session session = request.session();
        final String userName = request.queryParams(NAME_PARAM);
        Player player = new Player(userName);
        PlayerLobby playerLobby = session.attribute(GetHomeRoute.PLAYERLOBBY_KEY);
        playerLobby.addPlayer(player);
        vm.put(GetHomeRoute.TITLE_ATTR, "Welcome!");
        vm.put(GetHomeRoute.USER_ATTR, player);
        vm.put(GetHomeRoute.MESSAGE_ATTR, Message.info("Successfully logged in!"));
        ModelAndView mv = new ModelAndView(vm, GetHomeRoute.VIEW_NAME);
        return templateEngine.render(mv);
    }
}
