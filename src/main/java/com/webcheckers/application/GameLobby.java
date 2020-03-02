package com.webcheckers.application;

import com.webcheckers.Checkers.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GameLobby {

    // A list of unique players
    private List<Player> players;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public PlayerLobby() {
        this.players = new ArrayList<>();
    }

    /**
     * Get a new {@Linkplain PlayerServices} object to provide client-specific services to
     * the client who just connected to this application.
     *
     * @return
     *   A new {@Link PlayerServices}
     */
    public PlayerServices newPlayerServices(){
        LOG.fine("A new PlayerServices");
        return new PlayerServices(this);
    }

    /**
     * Add a player to the lobby
     * @param player the player to be added
     *
     * Pre-condition: containPlayer( player ) was called before
     *               being added
     */
    public void addPlayer( Player player ){
        this.players.add(player);
    }


    /**
     * Check whether the lobby already has the player with same name
     * @param player the player
     * @return true if the lobby already has the player, false otherwise
     */
    public boolean containPlayer(Player player){
        return this.players.contains(player);
    }

    /**
     * Remove the player from the lobby
     * @param player the player
     */
    public void removePlayer(Player player){
        this.players.remove(player);
    }
}
