package com.webcheckers.appl;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@Link PlayerLobby} component.
 *
 * @author Van Pham
 */
@Tag("Application-tier")
public class PlayerLobbyTest {

    // Objects used for testing
    String[] three_names = new String[] {"Player 1","Player 2","Player 3"};
    String[] zero_names = new String[] {};
    String[] one_names = new String[] {"Player 1"};


    // Friendly objects
    Player p1 = new Player("Player 1");
    Player p2 = new Player("Player 2");
    Player p3 = new Player("Player 3");
    Player p4 = new Player("Player 4");
    GameLobby gameLobby = new GameLobby(p1, 0);


    /**
     * Test the ability to make a new PlayerService.
     */
    @Test
    public void test_make_player_service() {
        final PlayerLobby CuT = new PlayerLobby();
        // Invoke test
        final PlayerServices playerSvc = CuT.newPlayerServices();
        // Analyze results
        assertNotNull(playerSvc);
    }

    /**
     * Test the ability to add a player to the lobby, delete
     *     a player from the lobby, and whether
     *     the lobby correctly remembers the player
     */
    @Test
    public void test_add_player(){
        final PlayerLobby CuT = new PlayerLobby();
        assertFalse(CuT.containPlayer(p1));
        CuT.addPlayer(p1);
        assertTrue(CuT.containPlayer(p1), "addPlayer() is flawed");
        CuT.removePlayer(p1);
        assertFalse(CuT.containPlayer(p1), "removePlayer() is flawed");
    }

    /**
     * Test the ability to correctly count the number of players in the lobby
     */
    @Test
    public void test_lobby_size(){
        final PlayerLobby CuT = new PlayerLobby();
        assertEquals(0, CuT.lobbySize(), "Lobby was not initially zero");
        CuT.addPlayer(p1);
        assertEquals(1, CuT.lobbySize(), "addPlayer did not change the lobby size");
        CuT.addPlayer(p2);
        assertEquals(2, CuT.lobbySize(), "addPlayer did not add a player");
        CuT.removePlayer(p1);
        assertEquals(1, CuT.lobbySize(), "removePlayer is not reflected on the lobbySize");
    }

    /**
     * Test the ability to create a list of available Player names (aka String)
     */
    @Test
    public void test_availablePlayers(){
        final PlayerLobby CuT = new PlayerLobby();
        boolean rlt = Arrays.equals(zero_names, CuT.availablePlayers().toArray(new String[0]));
        assertTrue(rlt);
        CuT.addPlayer(p1);
        rlt = Arrays.equals(one_names, CuT.availablePlayers().toArray(new String[0]));
        assertTrue(rlt);
        CuT.addPlayer(p2);
        CuT.addPlayer(p3);
        rlt = Arrays.equals(three_names, CuT.availablePlayers().toArray(new String[0]));
        assertTrue(rlt);
    }

    @Test
    public void testResigningandgetGame(){
        PlayerLobby CuT = new PlayerLobby();
        GameLobby g1 = new GameLobby(p1,1);
        g1.addPlayer(p2);
        GameLobby g2 = new GameLobby(p3, 2);
        g2.addPlayer(p4);
        g1.resign(p1);
        g2.addSpectator(new Player("joe"));
        CuT.addGame(g1);
        CuT.addGame(g2);
        assertNotNull(CuT.resignerOfGame(p1));
        assertNull(CuT.resignerOfGame(p2));
        assertNull(CuT.getGame(10));
        assertNotNull(CuT.getGame(1));
        BoardView board = new BoardView();
        assertNotNull(CuT.getBoard(g1));
        assertNotNull(CuT.getFlippedBoard(g1));
        assertNotNull(CuT.getSpecGame(new Player("joe")));

    }

    /**
     * Test addGame and removeGame. See whether the PlayerLobby knows that
     *     A player is in a game
     */
    @Test
    public void test_gameLobby(){
        final PlayerLobby CuT = new PlayerLobby();
        assertFalse(CuT.playerInGame(p1), "playerInGame has an error");
        CuT.addGame(gameLobby);
        assertTrue(CuT.playerInGame(p1), "player 1 should be in a game");
        assertEquals(CuT.playerOfGame(p1), gameLobby, "playerOfGame is broken");
        CuT.removeGame(gameLobby);
        assertFalse(CuT.playerInGame(p1));
        assertNull(CuT.playerOfGame(p1), "p1 is in a gameLobby");
    }


}
