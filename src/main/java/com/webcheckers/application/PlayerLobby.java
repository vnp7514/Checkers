package com.webcheckers.application;

import com.webcheckers.Checkers.Player;
import com.webcheckers.model.Board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The object to coordinate the state of the Web Application and keep all the players who have
 * signed into the game.
 *
 * @author Van Pham vnp7514@rit.edu
 */
public class PlayerLobby {

    // A list of unique players
    private Map<String, Player> players;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public PlayerLobby() {
        this.players = new HashMap<>();
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
        players.put(player.getName(), player);
    }


    /**
     * Check whether the lobby already has the player with same name
     * @param player the player
     * @return true if the lobby already has the player, false otherwise
     */
    public boolean containPlayer(Player player){
        return players.containsValue(player);
    }

    /**
     * Remove the player from the lobby
     * @param player the player
     */
    public void removePlayer(Player player){
        players.remove(player.getName());
    }

    /**
     * Get the size of the lobby
     * @return the size of the lobby
     */
    public int lobbySize() {
        return this.players.size();
    }

    /**
     * Return the a set of all the keys of the players map (aka the names of all
     *   players currently signed in)
     * @return a set
     */
    public Set<String> availablePlayers() {
        return players.keySet();
    }
}
