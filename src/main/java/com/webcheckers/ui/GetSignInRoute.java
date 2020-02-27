package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The {@code GET /signin} route handler.
 *
 * @author Van Pham <vnp7514@rit.edu>
 *
 */
public class GetSignInRoute implements Route {

    // Values used in the view-model map for rendering the signin view
    static final String VIEW_NAME = "signin.ftl";

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

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session httpSession = request.session();
        final Map<String,Object> vm = new HashMap<>();
        return templateEngine.render(new ModelAndView(vm,VIEW_NAME));
    }
}
