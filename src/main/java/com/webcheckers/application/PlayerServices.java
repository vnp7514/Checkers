package com.webcheckers.application;

import com.webcheckers.Checkers.Player;

/**
 * The object to coordinate the state of the Web Application.
 *
 * This class is an example of the GRASP Controller principle.
 *
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */

public class PlayerServices {

    private int totalGames = 0;
    private int wonGames = 0;
    // The current username of this player
    private Player player;

    // The playerLobby that contains all the current players in the game
    private final PlayerLobby playerLobby;

    /**
     * Construct a new PlayerServices but wait for player to sign in
     * @param playerLobby the playerLobby
     */
    PlayerServices(PlayerLobby playerLobby){
        this.playerLobby = playerLobby;
        player = null;
    }

    /**
     * Get the current player. Null if there is no player
     */
    public Player getPlayer(){
        return player;
    }
    /**
     * Cleanup
     */
    public void endSession(){

    }
}
