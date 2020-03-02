package com.webcheckers.Checkers;

import com.webcheckers.model.Space;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Row class represents a row of the board. Contains an ArrayList of Space classes.
 */
public class Row implements Iterable<Space> {

    private int index;
    private ArrayList<Space> spaces;
    private Space current;

    /**
     * Row constructor. Initializes the list of 8 spaces to contain no pieces.
     *
     * Initializes the index of the spaces.
     *
     * @param index the index of the board
     */
    public Row(int index) {
        this.index = index;
        //Create ArrayList of spaces without pieces
        this.spaces = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            this.spaces.add(new Space(i, null));
        }
    }

    /**
     * Returns the ArrayList as a Iterator<></>
     * @return An Iterator of the spaces
     */
    @Override
    public Iterator<Space> iterator() {
        return this.spaces.iterator();
    }

    /**
     * Getter for the index of the Row
     * @return
     */
    public int getIndex(){
        return this.index;
    }
}
