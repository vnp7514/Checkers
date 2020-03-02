package com.webcheckers.application;

import com.webcheckers.Checkers.Player;
import com.webcheckers.model.Board;

import java.util.*;
import java.util.logging.Logger;

/**
 * The object to coordinate the state of the Web Application and keep all the players who have
 * signed into the game.
 *
 * @author Van Pham vnp7514@rit.edu
 */
public class PlayerLobby {

    // A list of unique players
    private List<Player> players;
    private List<GameLobby> games;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public PlayerLobby() {
        this.games = new ArrayList<>();
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
    public List<String> availablePlayers() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < this.players.size(); i++) {
            names.add(i, this.players.get(i).getName());
        }
        return names;
    }

    /**
     * Add a new gameLobby to the list
     * @param gameLobby the game lobby
     */
    public void addGame(GameLobby gameLobby) {
        this.games.add(gameLobby);
    }

    /**
     * Remove the gameLobby from the list
     * @param gameLobby the game lobby
     */
    public void removeGame(GameLobby gameLobby) {
        this.games.remove(gameLobby);
    }


    /**
     * Check whether the player is in any gameLobby
     * @param player the player in question
     * @return true if the player is in a gameLobby
     */
    public boolean playerInGame(Player player) {
        for (GameLobby i : games) {
            if (i.containPlayer(player)) {
                return true;
            }
        }
        return false;
    }
}
