package com.webcheckers.Checkers;

import com.webcheckers.util.Message;

public class Player {

    /**
     * The name of the player
     */
    private final String name;

    /**
     * Create a player with the unique name
     * @param name the name of the player
     */
    public Player(final String name) {
        this.name = name;
    }

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return name;
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
