package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * The {@code GET /signin} route handler.
 *
 * @author Van Pham <vnp7514@rit.edu>
 *
 */
public class GetSignInRoute implements Route {

    // Values used in the view-model map for rendering the signin view
    static final String VIEW_NAME = "signin.ftl";
    static final String TITLE_ATTR = "title";

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /signin} route handler
     *
     * @param templateEngine
     *      The {@Link TemplateEngine} used for rendering page HTML.
     */
    GetSignInRoute(final TemplateEngine templateEngine){
        //validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();

        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        // Adding the title name
        vm.put(TITLE_ATTR, "WebChecker");

        final PlayerLobby playerLobby =
                httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);
        if (playerLobby == null){
            response.redirect(GetHomeRoute.VIEW_NAME);
            halt("Lobby is null");
            return null;
        } else {
            if (playerLobby.isFull()){
                response.redirect(GetHomeRoute.VIEW_NAME);
                halt("Lobby is full");
                return null;
            } else {

                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
            }
        }
    }
}
