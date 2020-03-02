package com.webcheckers.model;

public class Space {

    private int cellIdx;
    private Piece piece;

    public Space(int cellIdx, Piece piece) {
        this.cellIdx = cellIdx;
        this.piece = piece;
    }

    /**
     * Is valid checks to make sure that the board from a player's
     * perspective is both a black tile and does not already hold
     * a checker piece.
     * @return
     */
    public boolean isValid() {

        if (this.containsPiece()) {
            return false;
        }
        return true;
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
     * @return
     */
    public Piece getPiece() {
        return this.piece;
    }

    public Piece setPiece(Piece piece) {
        return this.piece = piece;
    }

    public boolean containsPiece() {
        if (this.piece == null) {
            return false;
        }
        return true;
    }


}
