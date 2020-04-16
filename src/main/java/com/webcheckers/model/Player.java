package com.webcheckers.model;

import com.webcheckers.appl.GameLobby;

public class Player {

    /**
     * The name of the player
     */
    private final String name;

    /**
     * A copy of the game lobby of this player's most recent completed game
     */
    private GameLobby gameLobbyCopy;

    /**
     * The game over message from this player's last game
     */
    private String gameOverMessage;

    /**
     * Create a player with the unique name
     * @param name the name of the player
     */
    public Player(final String name) {
        this.name = name;
        this.gameLobbyCopy = null;
        this.gameOverMessage = null;
    }

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Set the game lobby copy
     * @param lobby the game lobby to copy
     */
    public void setGameLobbyCopy( GameLobby lobby) {
        this.gameLobbyCopy = lobby;
    }

    /**
     * Get the game lobby copy
     * @return the game lobby
     */
    public GameLobby getGameLobbyCopy() {
        return this.gameLobbyCopy;
    }

    /**
     * set the game over message
     * @param message game over message
     */
    public void setGameOverMessage(String message) {
        this.gameOverMessage = message;
    }

    /**
     * Get the game over message
     * @return the game over message
     */
    public String getGameOverMessage() {
        return this.gameOverMessage;
    }
    /**
     * If obj is a Player instance and has the same name as this, return true
     * @param obj the object in question
     * @return true if they have the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        // Casting obj into Player
        Player o = (Player) obj;
        return o.getName().compareTo(this.getName()) == 0;
    }
}
