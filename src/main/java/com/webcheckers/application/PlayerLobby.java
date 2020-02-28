package com.webcheckers.application;

import com.webcheckers.Checkers.Player;

/**
 * The object to coordinate the state of
 */
public class PlayerLobby {

    // Player 1
    private Player player1 = null;
    // Player 2
    private Player player2 = null;

    /**
     * Add a player to the lobby
     * @param player the player to be added
     */
    public void addPlayer( Player player ){
        if (player1 == null){
            player1 = player;
        } else {
            player2 = player;
        }
    }
}
