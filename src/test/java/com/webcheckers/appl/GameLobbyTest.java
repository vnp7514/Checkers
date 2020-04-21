package com.webcheckers.appl;

import com.webcheckers.model.Color;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test for the {@Link GameLobby} component.
 *
 * @author Brody Wrighter
 */
@Tag("Application-tier")
public class GameLobbyTest {

    Player p1 = new Player("Player 1");
    Player p2 = new Player("Player 2");
    Player p3 = new Player("Player 3");
    Player p4 = new Player("Player 4");
    GameLobby gameLobby = new GameLobby(p1, 0);


    /**
     * Test the ability to make a new GameLobby.
     */
    @Test
    public void test_make_gamelobby() {
        //First constructor
        final GameLobby test1 = new GameLobby();
        assertNotNull(test1);
        //Second constructor
        final GameLobby test2 = new GameLobby(p1, 0);
        assertNotNull(test2);
    }

    /**
     * Test the ability to add players to the GameLobby
     */
    @Test
    public void test_add_players() {
        final GameLobby test1 = new GameLobby();
        test1.addPlayer(p1);
        assertTrue(test1.containPlayer(p1));
        test1.addPlayer(p2);
        assertTrue(test1.containPlayer(p2));
        assertFalse(test1.containPlayer(p3));
        test1.addPlayer(p3);
        assertTrue(test1.containPlayer(p3));
    }

    /**
     * Test the ability to remove players from the GameLobby
     */
    @Test
    public void test_remove_players() {
        //Adding
        final GameLobby test1 = new GameLobby();
        test1.addPlayer(p1);
        assertTrue(test1.containPlayer(p1));
        test1.addPlayer(p2);
        assertTrue(test1.containPlayer(p2));
        assertFalse(test1.containPlayer(p3));
        test1.addPlayer(p3);
        assertTrue(test1.containPlayer(p3));
        //Removing
        test1.removePlayer(p1);
        assertFalse(test1.containPlayer(p1));
        test1.removePlayer(p2);
        assertFalse(test1.containPlayer(p2));
        test1.removePlayer(p3);
        assertFalse(test1.containPlayer(p3));
    }

    /**
     * Test the ability to get the red and white players
     */
    @Test
    public void test_get_players() {
        final GameLobby test1 = new GameLobby();
        test1.addPlayer(p1);
        assertTrue(test1.containPlayer(p1));
        assertEquals(p1, test1.getRedPlayer());
        test1.addPlayer(p2);
        assertTrue((test1.containPlayer(p2)));
        assertNotEquals(p1, test1.getWhitePlayer());
        assertEquals(p2, test1.getWhitePlayer());
    }

    @Test
    public void runningTest(){
        GameLobby test1 = new GameLobby();
        test1.addPlayer(new Player("joe"));
        test1.addPlayer(new Player("john"));
        assertTrue(test1.isRunning());
        assertEquals(Color.RED,test1.getActiveColor());
        test1.end();
        assertEquals(Color.RED, test1.getActiveColor());
        assertFalse(test1.isRunning());
        test1 = new GameLobby();
        test1.addPlayer(new Player("joe"));
        test1.addPlayer(new Player("john"));
        test1.resign(new Player("joe"));
        assertEquals(new Player("joe"), test1.getQuitter());
        assertFalse(test1.isRunning());
        assertFalse(test1.containPlayer(new Player("joe")));
        assertTrue(test1.containPlayer(new Player("john")));

        test1 = new GameLobby();
        test1.swapActiveColor();
        assertEquals(Color.WHITE, test1.getActiveColor());

        test1 = new GameLobby(new Player("joe"), 1);
        assertEquals(1, test1.getGameID());
        test1.addPlayer(new Player("john"));
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        assertFalse(test1.hasSpectator());
        test1.addSpectator(p1);
        assertTrue(test1.hasSpectator());
        test1.removeSpectator(p1);
        assertFalse(test1.hasSpectator());
        test1.addSpectator(p1);
        assertTrue(test1.containSpectator(p1));
        assertFalse(test1.containSpectator(p2));
    }

    @Test
    public void testEquals(){
        GameLobby test1 = new GameLobby(new Player("joe"),1);
        test1.addPlayer(new Player("jone"));
        GameLobby test2 = new GameLobby(new Player("joe"),1);
        test2.addPlayer(new Player("jone"));
        assertTrue(test1.equals(test2));
        assertFalse(test1.equals(1));
    }

    @Test
    public void testToString(){
        final GameLobby test1 = new GameLobby();
        test1.addPlayer(new Player("john"));
        test1.addPlayer(new Player("bitc"));
        assertNotNull(test1.toString());
    }

    /**
     * Test the ability to get the board and the flipped board
     */
    @Test
    public void test_get_boards() {
        final GameLobby test1 = new GameLobby();
        test1.addPlayer(p1);
        assertTrue(test1.containPlayer(p1));
        test1.addPlayer(p2);
        assertTrue((test1.containPlayer(p2)));

        assertNotNull(test1.getBoard());
        assertNotNull(test1.getBoardFlipped());
    }
}
