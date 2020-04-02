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
     * The list of made moves
     */
    private ArrayList<Move> moves;

    /**
     * The BoardView Contructor, initializes an empty board with 8 rows (0 to 7)
     */
    public BoardView() {
        this.board = new Board();
        this.moves = new ArrayList<>();
        //Loop to create the Rows of the board(0 to 7)
        this.rows = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            this.rows.add(new Row(i));
        }
    }

    public BoardView(ArrayList board, ArrayList<Move> moves) {
        this.moves = moves;
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

    public Piece viewPiece(int row,int col) {
        return this.rows.get(row).viewPiece(col);
    }

    //TODO Stubbed out
    public void movePiece(Move move, BoardView board){
        int movesSize = board.moves.size();

        int endRow = board.seeTopMove().getEnd().getRow();
        int endCell = board.seeTopMove().getEnd().getCell();
        int startRow = board.moves.get(movesSize - 1).getStart().getRow();
        int startCell = board.moves.get(movesSize - 1).getStart().getCell();

        board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(board.getRow(startRow).getSpace(startCell).getPiece().getType(),board.getRow(startRow).getSpace(startCell).getPiece().getColor()));
        board.rows.get(startRow).getSpace(startCell).setPiece(null);
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
        Color playerColor;
        int movesSize = board.moves.size();
        if (movesSize >= 1) {
            int startRowI = board.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = board.moves.get(movesSize - 1).getStart().getCell();

            playerColor = board.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
        } else {
            playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
        }
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }

        int startRow;
        int endRow;

        if(playerColor == Color.WHITE) {
            startRow = move.getStart().getRow();
            endRow = move.getEnd().getRow();
        } else {

            endRow = move.getStart().getRow();
            startRow = move.getEnd().getRow();
        }

        if (board.seeTopMove() == null) {
            if (isValid(move.getStart().getRow(),move.getStart().getCell())) {
                if (isValid(move.getStart().getRow(),move.getStart().getCell())) {
                    if(endRow > startRow){ //Check that player is moving up
                        if (endRow-startRow == 1) { //Check if move up 1 space
                            return true;
                        } else if (isValidJump(move, board)) { //Check if skipping a piece
                            return true;
                        }
                    }
                }
            }
        } else if (isValidJump(board.seeTopMove(), board)) {
            if (isValid(startRow,endRow)) {
                if (isValid(startRow,endRow)) {
                    if(endRow > startRow){ //Check that player is moving up
                        if (endRow-startRow == 1) { //Check if move up 1 space
                            return false;
                        } else if (isValidJump(move, board)) { //Check if skipping a piece
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isValidJump(Move move, BoardView board) {
        int skipPieceRow, skipPieceCell = 0;

        Color playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();//Get player colors
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }

        int startRow;
        int endRow;

        if(playerColor == Color.WHITE) {

            startRow = move.getStart().getRow();
            endRow = move.getEnd().getRow();
        } else {
            endRow = move.getStart().getRow();
            startRow = move.getEnd().getRow();
        }

        if (startRow-endRow == 2) {
            skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
            skipPieceCell = (startRow + endRow) / 2; //Set skipped piece cell
            if (board.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece() != null) { //Check if piece exists
                if (board.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece().getColor() == otherPlayer) { //Check if piece is opposite color
                    return true;
                }
            }
        }
        return false;
    }

    public boolean newMoveExists(Move move, BoardView board) {
        if (board.moves.size() > 0) {
            if (!isValidJump(board.seeTopMove(), board)) {
                return false;
            }
        }

        Color playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();//Get player colors
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }

        int endRow = board.seeTopMove().getEnd().getRow();
        int endCell = board.seeTopMove().getEnd().getCell();

        if(playerColor == Color.WHITE) {
            if(endRow != 0) { //Check out of bounds
                if (board.getRow(endRow + 1).getSpace(endCell + 1).getPiece() != null) {
                    if (board.getRow(endRow + 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                        if (board.getRow(endRow + 2).getSpace(endCell + 2).getPiece() == null) {
                            return true;
                        }
                    }
                }
            }
            if(endRow != 7) { //Check out of bounds
                if (board.getRow(endRow + 1).getSpace(endCell - 1).getPiece() != null) {
                    if (board.getRow(endRow + 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) {
                        if (board.getRow(endRow + 2).getSpace(endCell - 2).getPiece() == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            if(endRow != 0) { //Check out of bounds
                if (board.getRow(endRow - 1).getSpace(endCell - 1).getPiece() != null) {
                    if (board.getRow(endRow - 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) {
                        if (board.getRow(endRow - 2).getSpace(endCell - 2).getPiece() == null) {
                            return true;
                        }
                    }
                }
            }
            if(endRow != 7) { //Check out of bounds
                if (board.getRow(endRow - 1).getSpace(endCell + 1).getPiece() != null) {
                    if (board.getRow(endRow - 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                        if (board.getRow(endRow - 2).getSpace(endCell + 2).getPiece() == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public BoardView flip()
    {
        ArrayList<Row> temp = new ArrayList<>();
        for (int i = 7; i >= 0; i--){
            temp.add(this.rows.get(i).flip());
        }
        return new BoardView(temp, this.moves);
    }

    /**
     * Adding a move to the list of moves
     * @param move the move to be added
     */
    public void addMove(Move move) {
        this.moves.add(0, move);
    }

    /**
     * Removing a move from list
     */
    public Move removeMove() {
        if (this.moves.isEmpty()){
            return null;
        }
        return this.moves.remove(0);
    }

    /**
     * Return the most recent move
     * @return null if moves is empty. Otherwise, the top element
     */
    public Move seeTopMove() {
        if (this.moves.isEmpty()) {
            return null;
        } else {
            return this.moves.get(0);
        }
    }

    /**
     * Remove all the moves from the list of moves
     */
    public void removeAllMoves() {
        while (seeTopMove() != null) {
            this.moves.remove(seeTopMove());
        }
    }

}
