package com.webcheckers.Checkers;

public class Player {

    /**
     * The name of the player
     */
    private final String name;

    /**
     * Create a player with the unique name
     * @param name the name of the player
     */
    Player(final String name) {
        this.name = name;
    }

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
    }
}
