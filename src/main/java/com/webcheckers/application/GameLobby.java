package com.webcheckers.application;

import com.webcheckers.Checkers.BoardView;
import com.webcheckers.Checkers.Player;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GameLobby {

    // A list of unique players
    private Player redPlayer;
    private Player whitePlayer;
    private BoardView board;
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public GameLobby(Player player) {
        this.redPlayer = player;
        this.whitePlayer = null;
        this.board = new BoardView();
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

    /**
     * Get the red player
     * @return the red player
     */
    public Player getRedPlayer()
    {
        return redPlayer;
    }

    /**
     * Get the white player
     * @return the white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
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
}
