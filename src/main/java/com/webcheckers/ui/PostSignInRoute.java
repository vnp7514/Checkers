package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSignInRoute implements Route{

    private final TemplateEngine templateEngine;

    static final String VIEW_NAME = "signin.ftl";
    static final String TITLE = "Please Sign In!";

    public PostSignInRoute(TemplateEngine templateEngine) {
        //Validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Map<String, Object> vm = new HashMap<>();
        vm.put("title", TITLE);
        return templateEngine.render(new ModelAndView(vm,VIEW_NAME));
    }
}
