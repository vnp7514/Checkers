package com.webcheckers.appl;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Color;
import com.webcheckers.model.Player;

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
    // A list of all games
    private List<GameLobby> games;
    // Number of games have been created/ Also used as a unique id for each game
    private int numGames = 0;
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
     * the client who just connected to this appl.
     *
     * @return
     *   A new {@Link PlayerServices}
     */
    public PlayerServices newPlayerServices(){
        LOG.fine("A new PlayerServices");
        return new PlayerServices();
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
     * Remove the player from the lobby and the gameLobby :)
     * @param player the player
     */
    public void removePlayer(Player player){
        this.players.remove(player);
        if (playerOfGame(player) != null){
            games.remove(playerOfGame(player));
        }
    }

    /**
     * Get the size of the lobby
     * @return the size of the lobby
     */
    public int lobbySize() {
        return this.players.size();
    }

    /**
     * Return the a list of all the players' name that the current client can
     *   challenge
     * @return a list
     */
    public List<String> availablePlayers() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            if (!playerInGame(player)) {
                names.add(player.getName());
            }
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

    /**
     * Return which GameLobby the player is in
     * @param player the player
     * @return the lobby that the player is in. null if
     * the player isnt in any lobby
     */
    public GameLobby playerOfGame(Player player)
    {
        for (GameLobby i : games) {
            if (i.containPlayer(player)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Get the board instance of the specific gameLobby
     * @param game the gameLobby
     * @return the board
     */
    public BoardView getBoard(GameLobby game)
    {
        return game.getBoard();
    }

    /**
     * Get the board instance of the specfic gameLobby
     * But the board is flipped
     * Used for rendering the challenged player board
     * @param game the game lobby
     * @return the flipped board
     */
    public BoardView getFlippedBoard(GameLobby game) {
        return game.getBoardFlipped();
    }
}
