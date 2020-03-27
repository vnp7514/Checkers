package com.webcheckers.model;

public class Space {

    private int cellIdx;
    private Piece piece;
    private int rowIdx;

    public Space(int cellIdx, Piece piece, int row) {
        this.cellIdx = cellIdx;
        this.piece = piece;
        this.rowIdx = row;
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
        } else if ((this.rowIdx % 2) == 0) {
            if ((this.cellIdx % 2) == 1) {
                return true;
            }
        } else if ((this.rowIdx % 2) == 1) {
            if ((this.cellIdx % 2) == 0) {
                return true;
            }
        }
        return false;
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
        if (this.isValid()) {
            return this.piece = piece;
        }
        return this.piece;
    }

    public boolean containsPiece() {
        if (this.piece == null) {
            return false;
        }
        return true;
    }

}
