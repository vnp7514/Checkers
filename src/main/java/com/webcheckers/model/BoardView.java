package com.webcheckers.model;

import java.util.*;
import com.webcheckers.util.Move;

/**
 * The Board representation, creates and Arraylist of Rows,
 *
 * Controls the alteration of the board for movements and initializing the game
 */
public class BoardView implements Iterable<Row> {

    private Board board;
    private ArrayList<Row> rows;

    /**
     * The BoardView Contructor, initializes an empty board with 8 rows (0 to 7)
     */
    public BoardView() {
        this.board = new Board();
        //Loop to create the Rows of the board(0 to 7)
        this.rows = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            this.rows.add(new Row(i));
        }
    }

    public BoardView(ArrayList board) {
        this.board = new Board();
        this.rows = board;
    }

    /**
     * Returns the Iterator of the Arraylist of Rows
     * @return An Iterator of the Rows
     */
    @Override
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }

    /**
     * Gives the instace of the Board
     *
     * *********************************
     * TODO Is this needed? Will we use the Board class?
     * *********************************
     *
     * @return
     */
    public Board getBoard(){
        return this.board;
    }

    public Row getRow(int indx) {
        return this.rows.get(indx);
    }

    public void setPiece(int row, int col, Piece piece) {
        this.rows.get(row).setSpace(col, piece);
    }

    //TODO Stubbed out
    public void movePiece(){

    }

    /**
     * Check whether the space at rowidx and cellidx (colidx) is
     *    a valid space (aka. a black title and does not hold a piece)
     * @param rowidx the row index
     * @param cellidx the cell index
     * @return true if it is valid. false otherwise
     */
    public boolean isValid(int rowidx, int cellidx){
        return board.isValid(rowidx, cellidx);
    }

    public boolean isValidMove(Move move, BoardView board){
        Color playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
        Color otherPlayer = Color.RED;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }

        int skipPieceRow, skipPieceCell = 0;
        if (isValid(move.getStart().getRow(),move.getStart().getCell())) {
            if (isValid(move.getStart().getRow(),move.getStart().getCell())) {
                if(move.getEnd().getCell() < move.getStart().getCell()){ //Check that player is moving up
                    if (move.getStart().getCell()-move.getEnd().getCell() == 1) { //Check if move up 1 space
                        return true;
                    } else if (move.getStart().getCell()-move.getEnd().getCell() == 2) { //Check if move up 2 space
                        skipPieceRow = (move.getStart().getRow() + move.getEnd().getRow()) / 2;
                        skipPieceCell = (move.getStart().getCell() + move.getEnd().getCell()) / 2;
                        if (board.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece().getColor() == otherPlayer) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public BoardView flip()
    {
        ArrayList<Row> temp = new ArrayList<>();
        for (int i = 7; i >= 0; i--){
            temp.add(this.rows.get(i).flip());
        }
        return new BoardView(temp);
    }
}
