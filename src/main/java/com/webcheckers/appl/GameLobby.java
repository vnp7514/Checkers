package com.webcheckers.appl;

import com.webcheckers.model.*;


import java.util.*;
import java.util.logging.Logger;

public class GameLobby {

    // A list of unique players
    //private Player redPlayer;
    //private Player whitePlayer;
    // a list of players currently in the game
    private List<Player> players;

    private List<Player> spectators;

    private int gameID;

    // The index where the specified player is in the list
    private int red = 0;
    private int white = 1;

    // Determine whether the Game is still running
    private boolean running = true;
    // This is the Player who has resigned the game
    private Player quitter = null;

    private Map<Player, Color> spectatorColor;
    private Color activeColor = Color.RED;

    // the game board
    private BoardView board;
    // For logging
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /**
     * A Constructor
     */
    public GameLobby() {
        this.players = new ArrayList<>(5);
        this.board = new BoardView();
        // TODO FOR TESTING ONLY
        /** For testing winConditions
        this.board = BoardView.testBoard();
        board.setPiece(4,5, new Piece(Type.SINGLE, Color.RED));
        board.setPiece(3,4, new Piece(Type.SINGLE, Color.WHITE));
        board.setPiece(1,4, new Piece(Type.SINGLE, Color.WHITE));
        board.setPiece(0,7, new Piece(Type.SINGLE, Color.WHITE));
         */
        /**
          this.board = new BoardView();
          board.setPiece(3,0, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(3,2, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(3,4, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(4,5, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(4,3, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(4,1, new Piece(Type.SINGLE,Color.RED));
          board.setPiece(3,6, new Piece(Type.SINGLE,Color.RED));
        */
        // TODO END OF TESTING
        this.spectators = new ArrayList<>();
        this.spectatorColor = new HashMap<>();
    }

    public GameLobby(Player player, int gameID){
        this();
        this.players.add(player);
        this.gameID = gameID;
    }

    /**
     * Return true if both Players are defined
     * @return true if both Players are not null
     */
    public boolean isRunning(){
        return running;
    }

    /**
     * End the game
     */
    public void end(){
        running = false;
    }

    /**
     * The player has resigned the game. The game is no longer running
     * @param player the player who has resigned
     */
    public void resign(Player player){
        running = false;
        quitter = player;
    }

    /**
     * Get the person who resigned
     * @return the Player instance that resigned
     */
    public Player getQuitter() {
        return quitter;
    }

    /**
     * Return the list of players in the game
     * @return the list of players
     */
    private List<Player> getPlayers() {
        return players;
    }

    public void swapActiveColor() {
        if (activeColor == Color.WHITE) {
            activeColor = Color.RED;
        }
        else {
            activeColor = Color.WHITE;
        }
    }

    public Color getActiveColor() {
        return activeColor;
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
     * Remove a player from the lobby
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
     * If the player has resigned, they are considered not in the game lobby
     * @param player the player
     * @return true if the lobby already has the player, false otherwise
     */
    public boolean containPlayer(Player player){
        if (player.equals(quitter)){
            return false;
        }
        return this.players.contains(player);
    }

    /**
     * Adds a spectator to the gamelobby
     * @param player
     */
    public void addSpectator(Player player) {
        this.spectators.add(player);
        this.spectatorColor.put(player, null);
    }

    /**
     * Removes a spectator from the gamelobby
     * @param player
     */
    public void removeSpectator(Player player) {
        this.spectators.remove(player);
        this.spectatorColor.remove(player);
    }

    /**
     * Check whether the lobby already has a spectator with the same name
     * @param player the spectator
     * @return true if lobby contains the spectator, otherwise false
     */
    public boolean containSpectator(Player player) { return this.spectators.contains(player); }

    /**
     * Return true if there are spectators
     * @return true if it is so
     */
    public boolean hasSpectator(){
        return !spectators.isEmpty();
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
     * Gets the gameID of this game lobby
     * @return int
     */
    public int getGameID() { return this.gameID; }

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

    public boolean specColor(Player player) {
        if (this.containSpectator(player)) {
            if (this.spectatorColor.get(player) != this.activeColor) {
                this.spectatorColor.put(player, this.activeColor);
                return true;
            }
        }
        return false;
    }

    /**
     * The name of the GameLobby instance is based on the names of the
     * two player names who are playing a game of checkers
     * @return String
     */
    @Override
    public String toString() {
        String p1 = "nobody";
        String p2 = "nobody";
        if (players.get(0) != null) {
            p1 = this.players.get(0).getName();
        }
        if (players.get(1)!= null) {
            p2 = this.players.get(1).getName();
        }
        return gameID + " : " + p1 + " vs " + p2;
    }

}
