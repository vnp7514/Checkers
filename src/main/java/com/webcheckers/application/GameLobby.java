package com.webcheckers.application;

import com.webcheckers.Checkers.BoardView;
import com.webcheckers.Checkers.Player;


import javax.swing.*;
import java.util.*;
import java.util.logging.Logger;

public class GameLobby {

    // A list of unique players
    //private Player redPlayer;
    //private Player whitePlayer;
    // a list of players currently in the game
    private List<Player> players;

    // The index where the specified player is in the list
    private int red = 0;
    private int white = 1;

    // the game board
    private BoardView board;
    // For logging
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public GameLobby() {
        this.players = new ArrayList<>();
        this.board = new BoardView();
    }

    public GameLobby(Player player){
        this();
        this.players.add(player);
    }

    /**
     * Return the list of players in the game
     * @return the list of players
     */
    private List<Player> getPlayers() {
        return players;
    }

    /**
     * Add a player to the lobby
     * @param player the player to be added
     *
     * Pre-condition: containPlayer( player ) was called before
     *               being added
     */
    public void addPlayer( Player player){
        this.players.add(player);
    }

    /**
     * Add a player to the lobby
     * @param player the player to be added
     *
     * Pre-condition: containPlayer( player ) was called before
     *               being added
     */
    public void removePlayer( Player player){
        this.players.remove(player);
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
     * Get the red player
     * @return the red player
     */
    public Player getRedPlayer()
    {
        return players.get(red);
    }

    /**
     * Get the white player
     * @return the white player
     */
    public Player getWhitePlayer() {
        return players.get(white);
    }

    /**
     * Get the board for the challenger
     * @return the board
     */
    public BoardView getBoard(){
        return this.board;
    }

    /**
     * Get the flipped board for player who got challenged
     * @return the flipped board
     */
    public BoardView getBoardFlipped(){
        return this.board.flip();
    }

    /**
     * Two gameLobbies are equal if they have the same players
     * @param obj the other gameLobby
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }
        if (!(obj instanceof GameLobby)) {
            return false;
        }
        GameLobby o = (GameLobby)obj;
        return Arrays.equals(players.toArray(new Player[0]),
                o.getPlayers().toArray(new Player[0]));
    }
}
