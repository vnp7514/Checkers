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

    /**
     * Handles the movements of the piece based on the stored moves in the moves array list(Part of BoardView)
     *
     *
     * @param move
     * @param board
     */
    public void movePiece(Move move, BoardView board){
        int movesSize = board.moves.size();

        //-----------------------------------------------------------------
        //  COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        Color playerColor;
        Type pieceType;
        if (movesSize >= 1) {
            int startRowI = board.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = board.moves.get(movesSize - 1).getStart().getCell();

            playerColor = board.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = board.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        //  For each loop, goes through each move in the list of moves and handles
        // moving the piece as well as destroying jumped over pieces
        //
        // Also handles the Kingking of pieces
        //-----------------------------------------------------------------
        for (Move m : board.moves) {
            int endRow = m.getEnd().getRow();
            int endCell = m.getEnd().getCell();
            int startRow = m.getStart().getRow();
            int startCell = m.getStart().getCell();

            if (startRow < endRow) { //Player is a White player
                if (endRow-startRow == 1) { // Check if it was a single move
                    if (endRow == 7) { // Check if the piece needs to be kinged
                        board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    board.rows.get(startRow).getSpace(startCell).setPiece(null); // Remove the piece from the start position
                } else if (endRow - startRow == 2) { // Check if it was a jump move
                    int skipPieceRow = (startRow + endRow) / 2; //The row of the piece that has been jumped over
                    int skipPieceCell = (startCell + endCell) / 2; //The row of the piece that has been jumped over
                    board.rows.get(startRow).getSpace(startCell).setPiece(null); // Remove the piece from the start position
                    board.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null); // Remove the jumped over piece from the start position
                    if (m.equals(board.seeTopMove())) { //Check if it is the last move/jump
                        if (endRow == 7) { //Check if the piece needs to be kinged
                            board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                        } else {
                            board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                        }
                    }

                }
            } else if (endRow < startRow) { //Player is a Red Player
                if (startRow-endRow == 1) {// Check if it was a single move
                    if (endRow == 0) {// Check if the piece needs to be kinged
                        board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    board.rows.get(startRow).getSpace(startCell).setPiece(null);
                } else if (startRow - endRow == 2) {
                    int skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                    int skipPieceCell = (startCell + endCell) / 2; //Set skipped piece cell
                    board.rows.get(startRow).getSpace(startCell).setPiece(null);// Remove the piece from the start position
                    board.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null);// Remove the jumped over piece from the start position
                    if (m.equals(board.seeTopMove())) {//Check if it is the last move/jump
                        if (endRow == 0) {// Check if the piece needs to be kinged
                            board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                        } else {
                            board.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                        }
                    }
                }
            }
        }
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


    /**
     * Check if the players move was a valid move to make
     *  - Checks that the player is going the right way
     *  - Checks the player is going diagonally
     *  - Checks to make sure jumps are valid jumps
     * @param move The move that the player made
     * @param board The current Boardview
     * @return
     */
    public boolean isValidMove(Move move, BoardView board){
        Color playerColor;
        int movesSize = board.moves.size();
        Type pieceType;
        //-----------------------------------------------------------------
        //  COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        if (movesSize >= 1) {
            int startRowI = board.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = board.moves.get(movesSize - 1).getStart().getCell();

            playerColor = board.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = board.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // SET OTHER PLAYER COLOR
        //-----------------------------------------------------------------
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }
        //-----------------------------------------------------------------

        int startRow;
        int endRow;

        //-----------------------------------------------------------------
        // Get start and end rows
        //-----------------------------------------------------------------
        if(playerColor == Color.WHITE) {
            startRow = move.getStart().getRow();
            endRow = move.getEnd().getRow();
        } else {
            endRow = move.getStart().getRow();
            startRow = move.getEnd().getRow();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Begin validation
        //-----------------------------------------------------------------
        if (board.seeTopMove() == null) { // Check if its the first move
            if(endRow > startRow || ((pieceType == Type.KING) && startRow > endRow)){ //Check that player is moving up
                if (endRow-startRow == 1 || ((pieceType == Type.KING) && (endRow-startRow == -1))) { //Check if move up 1 space
                    if (move.getEnd().getCell()-move.getStart().getCell() == 1 || move.getEnd().getCell()-move.getStart().getCell() == -1) { // make sure that the player doesnt go too far left or right
                        return true;
                    }
                } else if (isValidJump(move, board)) { //Check if skipping a piece, go to is valid jump
                    return true;
                }
            }
        } else if (isValidJump(board.seeTopMove(), board)) {
            if(endRow > startRow ||  ((pieceType == Type.KING) && startRow > endRow)) { //Check that player is moving up
                if (endRow-startRow == 1  || ((pieceType == Type.KING) && (endRow-startRow == -1))) { //Check if move up 1 space
                    return false;
                } else if (isValidJump(move, board)) { //Check if skipping a piece
                    return true;
                }
            }
        }
        return false;
        //-----------------------------------------------------------------
    }

    /**
     * Check if the move made by the player is a valid jump move
     * @param move The move that the player
     * @param board The current BoardView
     * @return boolean
     */
    public boolean isValidJump(Move move, BoardView board) {
        int skipPieceRow, skipPieceCell = 0;
        Color playerColor;
        Type pieceType;
        int movesSize = board.moves.size();

        //-----------------------------------------------------------------
        // COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        if (movesSize >= 1) {
            int startRowI = board.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = board.moves.get(movesSize - 1).getStart().getCell();

            playerColor = board.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = board.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Get other player color
        //-----------------------------------------------------------------
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }
        //-----------------------------------------------------------------

        int startRow;
        int endRow;

        //-----------------------------------------------------------------
        // Get start an end rows
        //-----------------------------------------------------------------
        if(playerColor == Color.WHITE) {
            startRow = move.getStart().getRow();
            endRow = move.getEnd().getRow();
        } else {
            endRow = move.getStart().getRow();
            startRow = move.getEnd().getRow();
        }
        //-----------------------------------------------------------------

        //-----------------------------------------------------------------
        // Check if valid jump
        //-----------------------------------------------------------------
        if (endRow-startRow == 2 || ((pieceType == Type.KING)&&(endRow-startRow == -2))) {
            if (move.getEnd().getCell()-move.getStart().getCell() == 2 || move.getEnd().getCell()-move.getStart().getCell() == -2) { // check if the player moves up or down two rows
                skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                skipPieceCell = (move.getStart().getCell() + move.getEnd().getCell()) / 2; //Set skipped piece cell
                if (board.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece() != null) { //Check if piece exists
                    if (board.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece().getColor() == otherPlayer) { //Check if piece is opposite color
                        return true;
                    }
                }
            }
        }
        return false;
        //-----------------------------------------------------------------
    }

    /**
     * Checks if there is another available jump for the player
     * @param move The last move of the player
     * @param board The current BoardView
     * @return boolean
     */
    public boolean newMoveExists(Move move, BoardView board) {
        if (board.moves.size() > 0) {
            if (!isValidJump(board.seeTopMove(), board)) {
                return false;
            }
        }

        //-----------------------------------------------------------------
        // COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        Color playerColor;
        Type pieceType;
        int movesSize = board.moves.size();
        if (movesSize >= 1) {
            int startRowI = board.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = board.moves.get(movesSize - 1).getStart().getCell();

            playerColor = board.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = board.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = board.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
        }
        //-----------------------------------------------------------------

        //-----------------------------------------------------------------
        // Get other player color
        //-----------------------------------------------------------------
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Get start and end positions
        //-----------------------------------------------------------------
        int endRow = board.seeTopMove().getEnd().getRow();
        int endCell = board.seeTopMove().getEnd().getCell();
        int startRow = board.seeTopMove().getStart().getRow();
        int startCell = board.seeTopMove().getStart().getCell();
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Begin checking for an available move
        //-----------------------------------------------------------------
        if(playerColor == Color.WHITE || pieceType == Type.KING) { // Check if its a White player
            if(endCell < 6) { //Check out of bounds
                if (endRow < 6) { //Check out of bounds
                    if (board.getRow(endRow + 1).getSpace(endCell + 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                        if (board.getRow(endRow + 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                            if ((board.getRow(endRow + 2).getSpace(endCell + 2).getPiece() == null) && ((endRow + 2 != startRow) || (endCell + 2 != startCell))) { //Check the space is free to jump
                                return true;
                            }
                        }
                    }
                }
            }
            if(endCell > 1) { //Check out of bounds
                if (endRow < 6) { //Check out of bounds
                    if (board.getRow(endRow + 1).getSpace(endCell - 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                        if (board.getRow(endRow + 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                            if ((board.getRow(endRow + 2).getSpace(endCell - 2).getPiece() == null) && ((endRow + 2 != startRow) || (endCell - 2 != startCell))) { //Check the space is free to jump
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if (playerColor == Color.RED || pieceType == Type.KING){
            if((endCell > 1) && (endRow > 1)) { //Check out of bounds
                if (board.getRow(endRow - 1).getSpace(endCell - 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                    if (board.getRow(endRow - 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                        if ((board.getRow(endRow - 2).getSpace(endCell - 2).getPiece() == null) && ((endRow - 2 != startRow) || (endCell - 2 != startCell))) { //Check the space is free to jump
                            return true;
                        }
                    }
                }
            }
            if((endRow > 1) && (endCell < 6)) { //Check out of bounds
                if (board.getRow(endRow - 1).getSpace(endCell + 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                    if (board.getRow(endRow - 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                        if ((board.getRow(endRow - 2).getSpace(endCell + 2).getPiece() == null) && ((endRow - 2 != startRow) || (endCell + 2 != startCell))) { //Check the space is free to jump
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

    public boolean winCondition(){
        int red = 0;
        int whi= 0;
        for (Row r : this.rows) {
            for (Space s : r) {
                if (s.getPiece() != null) {
                    if (s.getPiece().getColor() == Color.WHITE) {
                        whi += 1;
                    } else {
                        red += 1;
                    }
                }
            }
        }

        if (whi == 0 || red == 0) {
            return true;
        } else {
            return false;
        }
    }
}
