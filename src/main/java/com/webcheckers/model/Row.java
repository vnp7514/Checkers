package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Row class represents a row of the board. Contains an ArrayList of Space classes.
 */
public class Row implements Iterable<Space> {

    private int index;
    private ArrayList<Space> spaces;

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
            if (this.index == 0) {
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

    public ArrayList<Space> getSpaces() {
        return spaces;
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
     * Get the Piece at the space with the specified column index
     * @param col the column index
     * @return the Piece at that Space
     */
    public Piece viewPiece(int col) {
        Space s = getSpace(col);
        if (s != null){
            return s.getPiece();
        }
        return null;
    }

    /**
     * Get the Space at the specified index
     * @param index the index of the Space that is occupied
     * @return the Space
     */
    public Space getSpace(int index) {
        for (Space s :spaces){
            if(s.getCellIdx() == index){
                return s;
            }
        }
        return null;
    }

    /**
     * Put a piece on the space
     * @param spaceID the column idx (on the board) of the space
     *              or the idx of the space on the row counting from left to right
     * @param piece the piece
     */
    public void setSpace(int spaceID, Piece piece) {
        this.getSpace(spaceID).setPiece(piece);
    }

    /**
     * Remove the piece from the space
     * @param spaceID the idx of the space
     */
    public void clearSpace(int spaceID){
        this.getSpace(spaceID).removePiece();
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

    /**
     * Return a representation of the Row. Ex:
     * 2 | |W| |R| |r|
     *  where 2 is the indx of the row
     * @return the String representation
     */
    @Override
    public String toString() {
        String res = this.getIndex() + " |";
        for (Space space : spaces){
            res = res.concat(" " + space.toString() + " |");
        }
        return res;
    }
}
