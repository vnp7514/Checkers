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

    /**
     * Return true if the lobby is full, false otherwise
     * @return ^
     */
    public boolean isFull(){
        if (player1 != null && player2 != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get player 1
     * @return player 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Get player 2
     * @return player 2
     */
    public Player getPlayer2() {
        return player2;
    }
}
