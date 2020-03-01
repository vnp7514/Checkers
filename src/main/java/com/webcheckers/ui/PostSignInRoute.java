package com.webcheckers.ui;

import com.webcheckers.Checkers.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostSignInRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());


    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;


    // Messages for Username error checking
    private static final Message USER_ALREADY_DEFINED =
            Message.error("This username has been taken!");
    private static final Message AT_LEAST_ALPHANUM =
            Message.error("The name needs to contain at least one " +
                    "alphanumeric character!");
    private static final Message NO_ALPHANUM =
            Message.error("The name cannot have one or more characters" +
                    " that are not alphanumeric or spaces!");


    // Values used in the View-Model map for rendering the page
    static final String VIEW_NAME = "signin.ftl";
    static final String NAME_PARAM = "username";


    /**
     * The constructor for the code {@code POST /signin} route handler.
     *
     * @param templateEngine the template engine to use for rendering HTML page
     * @param playerLobby the player Lobby
     *
     * @throws NullPointerException
     *     when the {@code templateEngine} is null
     */
    public PostSignInRoute(final PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        //Validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * Check whether this is a valid username
     * @param userName the name
     * @return null if it is a valid name. Otherwise, a specific Message
     *         will be returned
     */
    private Message checkUsername(String userName){
        //TODO
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.fine("PostSignInRoute is called!");
        // View-model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Sign In");
        // retrieve the HTTP connection
        final Session session = request.session();

        final String userName = request.queryParams(NAME_PARAM);
        // Checking whether this is a valid name
        Message message = checkUsername(userName);

        if (message == null) {

            LOG.fine("Username is valid!");
            Player player = new Player(userName);
            PlayerServices playerServices =
                    session.attribute(GetHomeRoute.PLAYER_KEY);

            if (playerServices == null) {
                LOG.fine("PlayerServices is null");
                response.redirect(WebServer.HOME_URL);
                halt("PlayerServices is null");
                return null;
            } else {
                LOG.fine("PlayerServices is not null");
                if (playerServices.getPlayer() == null) {
                    LOG.fine("This session has not signed in!");
                    if (playerLobby.containPlayer(player)) {
                        vm.put(GetHomeRoute.MESSAGE_ATTR, USER_ALREADY_DEFINED);
                    } else {
                        playerLobby.addPlayer(player);
                        playerServices.insertPlayer(player);
                        response.redirect(WebServer.HOME_URL);
                        halt();
                        return null;
                    }
                } else {
                    LOG.fine("This session has signed in!");
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
            }
        } else {
            vm.put(GetHomeRoute.MESSAGE_ATTR, message);
        }
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
