package com.webcheckers.application;

import com.webcheckers.Checkers.Player;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GameLobby {

    // A list of unique players
    private Player redPlayer;
    private Player whitePlayer;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public GameLobby(Player player) {
        this.redPlayer = player;
        this.whitePlayer = null;
    }

    /**
     * Add a player to the lobby
     * @param player the player to be added
     *
     * Pre-condition: containPlayer( player ) was called before
     *               being added
     */
    public void addPlayer( Player player){
        if (this.whitePlayer == null) {
            this.whitePlayer = player;
        }
    }


    /**
     * Check whether the lobby already has the player with same name
     * @param player the player
     * @return true if the lobby already has the player, false otherwise
     */
    public boolean containPlayer(Player player){
        if (this.whitePlayer.equals(player)){
            return true;
        }
        else if( this.redPlayer.equals(player)){
            return true;
        } else {
            return false;
        }
    }
}
