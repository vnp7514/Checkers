package com.webcheckers.model;

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
            if (this.index == 0)
            {
                if ((i % 2) == 1){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.WHITE), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            } else if (this.index == 1) {
                if ((i % 2) == 0){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.WHITE), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            } else if (this.index == 2) {
                if ((i % 2) == 1){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.WHITE), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            }else if (this.index == 5) {
                if ((i % 2) == 0){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.RED), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            }else if (this.index == 6) {
                if ((i % 2) == 1){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.RED), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            }else if (this.index == 7) {
                if ((i % 2) == 0){
                    this.spaces.add(new Space(i, new Piece(Type.SINGLE, Color.RED), this.index));
                } else {
                    this.spaces.add(new Space(i, null, this.index));
                }
            } else {
                this.spaces.add(new Space(i, null, this.index));
            }
        }
    }

    public Row(int index, ArrayList<Space> spaces){
        this.spaces = spaces;
        this.index = index;
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
     * @return the index of the row
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Put a piece on the space
     * @param space the space
     * @param piece the piece
     */
    public void setSpace(int space, Piece piece) {
        this.spaces.get(space).setPiece(piece);
    }

    /**
     * Flip the row for the other player
     * @return the flipped row
     */
    public Row flip() {
        ArrayList<Space> temp = new ArrayList<>();
        for(int i = 7; i >= 0; i--){
            temp.add(this.spaces.get(i));
        }
        return new Row(this.index, temp);
    }
}
