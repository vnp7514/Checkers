package com.webcheckers.model;

/**
 * A class that represents a space on a board
 */
public class Space {

    // The column of the board that this space is on
    private int cellIdx;
    // The piece that is on this space
    private Piece piece;
    // The row of the board that this space is on
    private int rowIdx;

    /**
     * A constructor
     * @param cellIdx the column id of the space
     * @param piece the piece that this space has
     * @param row the row id of the space
     */
    public Space(int cellIdx, Piece piece, int row) {
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.rowIdx = row;
    }

    /**
     * Get the cellIdx number
     * @return int
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * Get the piece
     * @return the Piece
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Is valid checks to make sure that the board from a player's
     * perspective is both a black tile and does not already hold
     * a checker piece.
     * @return true if the above condition is true, false otherwise
     */
    public boolean isValid() {
        return !(this.containsPiece()) && this.isABLackTitle();
    }

    /**
     * Put a piece on this space if it is black and unoccupied
     * @param piece the piece to be put on
     *
     * WARNING: do nothing if the space is occupied or not black
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Remove the piece from this space
     */
    public void removePiece(){
        this.piece = null;
    }
    /**
     * Return true if this is a black title on the board
     * @return the true if the above condition is true, false otherwise
     */
    private boolean isABLackTitle(){
        // Counting all rows and cells from 0

        // Every even row should have its odd-index cell black
        if ((this.rowIdx % 2) == 0) {
            if ((this.cellIdx % 2) == 1) {
                return true;
            }
        }
        // Every odd row should have its even-index cell black
        else if ((this.rowIdx % 2) == 1) {
            if ((this.cellIdx % 2) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if this space has no piece on it
     * @return true if the above is true, false otherwise.
     */
    private boolean containsPiece() {
        if (this.piece == null) {
            return false;
        }
        return true;
    }

    /**
     * The String representation of the piece
     * if there is no piece then a space " " is return
     * if there is a piece then then the abbreviation of the piece is return
     * Ex: White single piece returns "w"
     * @return the String representation
     */
    @Override
    public String toString() {
        if (piece == null){
            return " ";
        } else return piece.toString();
    }
}
