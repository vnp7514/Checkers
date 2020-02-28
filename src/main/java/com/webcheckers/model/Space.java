package com.webcheckers.model;

public class Space {

    private int cellIdx;
    private Piece piece;

    public Space(int cellIdx, Piece piece) {
        this.cellIdx = cellIdx;
        this.piece = piece;
    }

    // Stubbed out
    public boolean isValid() {
        return true;
    }

    public int getCellIdx() {
        return cellIdx;
    }

    public Piece getPiece() {
        return piece;
    }

}
