package com.webcheckers.application;

import com.webcheckers.Checkers.BoardView;
import com.webcheckers.Checkers.Player;
import com.webcheckers.model.Board;
import com.webcheckers.util.Message;

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
    // String representing the availability of the player
    private String message;

    private BoardView game;

    /**
     * Construct a new PlayerServices but wait for player to sign in
     * @param playerLobby the playerLobby
     */
    PlayerServices(PlayerLobby playerLobby){
        this.playerLobby = playerLobby;
        player = null;
        this.message = null;
    }



    /**
     * Get the current player. Null if there is no player
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Insert the new player to this PlayerServices if this is not holding any
     * This is used when player has successfully signed in
     * @param player the player
     */
    public void insertPlayer(Player player){
        if (this.player == null){
            this.player = player;
        }
    }

    /**
     * Delete the current player in this PlayerServices
     * This is used when the player has signed out
     */
    public void deletePlayer(){
        this.player = null;
    }

    /**
     * Cleanup when the session is over
     */
    public void endSession(){

    }

    /**
     * This function stores a given message into
     * the message variable
     * @param message String
     */
    public void storeMessage(String message) {
        this.message = message;
    }

    /**
     * This function returns the String message value
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * This function sets the message string to null
     */
    public void removeMessage() {
        this.message = null;
    }
}
