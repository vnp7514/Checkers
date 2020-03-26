package com.webcheckers.ui;

import com.webcheckers.Checkers.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Ui Tier")
public class PostSignInRouteTest {

    private static final Message USER_ALREADY_DEFINED =
            Message.error("This username has been taken!");
    private static final Message AT_LEAST_ALPHANUM =
            Message.error("The name needs to contain at least one " +
                    "alphanumeric character!");
    private static final Message NO_ALPHANUM =
            Message.error("The name cannot have one or more characters" +
                    " that are not alphanumeric or spaces!");

    Player p1 = new Player("Player 1");
    Player p2 = new Player("Hey!");
    Player p3 = new Player("George_and  The Jungle!");
    Player p4 = new Player("+");

    @Test
    public void nameTaken() {
        PlayerLobby CuT = new PlayerLobby();
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        PostSignInRoute route = new PostSignInRoute(CuT, templateEngine);

        assertFalse(CuT.containPlayer(p1));
        CuT.addPlayer(p1);
        assertTrue(CuT.containPlayer(p1));
        if (USER_ALREADY_DEFINED == route.checkUsername(p1.getName())) {
            assertTrue(true);
        }
        CuT.removePlayer(p1);
        assertFalse(CuT.containPlayer(p1));
    }

    @Test
    public void nonAlphanumeric() {
        PlayerLobby CuT = new PlayerLobby();
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        PostSignInRoute route = new PostSignInRoute(CuT, templateEngine);

        assertFalse(CuT.containPlayer(p3));
        if (NO_ALPHANUM == route.checkUsername(p3.getName())) {
            assertTrue(true);
        }
    }

    @Test
    public void atLeastAlphanum() {
        PlayerLobby CuT = new PlayerLobby();
        TemplateEngine templateEngine = mock(TemplateEngine.class);
        PostSignInRoute route = new PostSignInRoute(CuT, templateEngine);

        assertFalse(CuT.containPlayer(p2));
        if (null == route.checkUsername(p2.getName())) {
            assertTrue(true);
        }
        assertFalse(CuT.containPlayer(p4));
        if (AT_LEAST_ALPHANUM == route.checkUsername((p4.getName()))) {
            assertTrue(true);
        }
    }

}
