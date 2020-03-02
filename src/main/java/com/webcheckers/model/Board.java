package com.webcheckers.model;

public class Board {

    // number of rows in a Checkers board
    private static final int NUM_ROWS = 8;

    // number of columns in a Checkers board
    private static final int NUM_COLS = 8;

    // 2D array representation of the board
    private Space[][] board;

    public Board() {
        this.board = new Space[NUM_ROWS][NUM_COLS];
        initialize_board();
    }

    /**
     * Creates the starting board
     * White checkers are placed in the top three rows, red checkers on the bottom three rows
     */
    private void initialize_board() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (col % 2 == 1 && row == 1) {
                    board[row][col] = new Space(col, new Piece(Type.SINGLE, Color.WHITE));
                } else if (col % 2 == 0 && row < 3) {
                    board[row][col] = new Space(col, new Piece(Type.SINGLE, Color.WHITE));
                } else if (col % 2 == 0 && row == 6) {
                    board[row][col] = new Space(col, new Piece(Type.SINGLE, Color.RED));
                } else if (col % 2 == 1 && row > 4) {
                    board[row][col] = new Space(col, new Piece(Type.SINGLE, Color.RED));
                } else {
                    board[row][col] = new Space(col, null);
                }
            }
        }
    }


}
