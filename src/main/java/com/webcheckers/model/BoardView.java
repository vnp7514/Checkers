package com.webcheckers.model;

import java.util.*;
import com.webcheckers.util.Move;

/**
 * The Board representation, creates and Arraylist of Rows,
 *
 * Controls the alteration of the board for movements and initializing the game
 */
public class BoardView implements Iterable<Row> {

    /**
     * The list of rows in the board
     */
    private ArrayList<Row> rows;

    /**
     * Operates like a Stack. First in Last Out
     */
    private ArrayList<Move> moves;

    /**
     * The BoardView Contructor, initializes an empty board with 8 rows (0 to 7)
     */
    public BoardView() {
        this.moves = new ArrayList<>();
        //Loop to create the Rows of the board(0 to 7)
        this.rows = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            this.rows.add(new Row(i));
        }
    }

    public BoardView(ArrayList<Row> board, ArrayList<Move> moves) {
        this.moves = moves;
        this.rows = board;
    }

    /**
     * Create an empty board that is used for testing
     * @return a BoardView instance with all Spaces initialized with null
     */
    public static BoardView testBoard(){
        ArrayList<Row> list_rows = new ArrayList<>();
        for (int i =0; i <= 7; i++){
            ArrayList<Space> list_spaces = new ArrayList<>();
            for (int a=0; a<=7;a++){
                 list_spaces.add(new Space(a,null,i));
            }
            list_rows.add(new Row(i, list_spaces));
        }
        return new BoardView(list_rows, new ArrayList<>());
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
     * Get the Row that has the index
     * @param indx the index of the Row to be found
     * @return the Row if found. null otherwise.
     */
    private Row getRow(int indx) {
        for(Row row : rows){
            if(row.getIndex() == indx){
                return row;
            }
        }
        return null;
    }

    /**
     * Set the piece at the specified row or col into the specifed piece
     * @param row the row indx of the space
     * @param col the col index of the space
     * @param piece the piece to be put there
     */
    public void setPiece(int row, int col, Piece piece) {
        this.getRow(row).setSpace(col, piece);
    }

    /**
     * View the piece that is at this coordinate
     * @param row the row index
     * @param col the col index
     * @return the piece at that space
     */
    public Piece viewPiece(int row,int col) {
        return this.getRow(row).viewPiece(col);
    }


    /**
     * Check whether the space at rowidx and cellidx (colidx) is
     *    a valid space (aka. a black title and does not hold a piece)
     * @param rowidx the row index
     * @param cellidx the cell index
     * @return true if it is valid. false otherwise
     */
    public boolean isValid(int rowidx, int cellidx){
        return this.getRow(rowidx).getSpace(cellidx).isValid();
    }

    /**
     * Handles the movements of the piece based on the stored moves in the moves array list(Part of BoardView)
     */
    public void movePiece(){
        int movesSize = this.moves.size();
        Move move = this.seeTopMove();

        //-----------------------------------------------------------------
        //  COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        Color playerColor;
        Type pieceType;
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = this.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        //  For each loop, goes through each move in the list of moves and handles
        // moving the piece as well as destroying jumped over pieces
        //
        // Also handles the Kingking of pieces
        //-----------------------------------------------------------------
        for (Move m : this.moves) {
            int endRow = m.getEnd().getRow();
            int endCell = m.getEnd().getCell();
            int startRow = m.getStart().getRow();
            int startCell = m.getStart().getCell();

            if (startRow < endRow) { //Player is a White player
                if (endRow-startRow == 1) { // Check if it was a single move
                    if (endRow == 7) { // Check if the piece needs to be kinged
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    this.rows.get(startRow).getSpace(startCell).setPiece(null); // Remove the piece from the start position
                } else if (endRow - startRow == 2) { // Check if it was a jump move
                    int skipPieceRow = (startRow + endRow) / 2; //The row of the piece that has been jumped over
                    int skipPieceCell = (startCell + endCell) / 2; //The row of the piece that has been jumped over
                    this.rows.get(startRow).getSpace(startCell).setPiece(null); // Remove the piece from the start position
                    this.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null); // Remove the jumped over piece from the start position
                    if (m.equals(this.seeTopMove())) { //Check if it is the last move/jump
                        if (endRow == 7) { //Check if the piece needs to be kinged
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                        } else {
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                        }
                    }

                }
            } else if (endRow < startRow) { //Player is a Red Player
                if (startRow-endRow == 1) {// Check if it was a single move
                    if (endRow == 0) {// Check if the piece needs to be kinged
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);
                } else if (startRow - endRow == 2) {
                    int skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                    int skipPieceCell = (startCell + endCell) / 2; //Set skipped piece cell
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);// Remove the piece from the start position
                    this.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null);// Remove the jumped over piece from the start position
                    if (m.equals(this.seeTopMove())) {//Check if it is the last move/jump
                        if (endRow == 0) {// Check if the piece needs to be kinged
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                        } else {
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if the players move was a valid move to make
     *  - Checks that the player is going the right way
     *  - Checks the player is going diagonally
     *  - Checks to make sure jumps are valid jumps
     * @param move The move that the player made
     * @return
     */
    public boolean isValidMove(Move move){
        Color playerColor;
        int movesSize = this.moves.size();
        Type pieceType;
        //-----------------------------------------------------------------
        //  COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = this.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
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
        if (this.seeTopMove() == null) { // Check if its the first move
            if(endRow > startRow || ((pieceType == Type.KING) && startRow > endRow)){ //Check that player is moving up
                if (endRow-startRow == 1 || ((pieceType == Type.KING) && (endRow-startRow == -1))) { //Check if move up 1 space
                    if (move.getEnd().getCell()-move.getStart().getCell() == 1 || move.getEnd().getCell()-move.getStart().getCell() == -1) { // make sure that the player doesnt go too far left or right
                        return true;
                    }
                } else if (isValidJump(move)) { //Check if skipping a piece, go to is valid jump
                    return true;
                }
            }
        } else if (isValidJump(this.seeTopMove())) {
            if(endRow > startRow ||  ((pieceType == Type.KING) && startRow > endRow)) { //Check that player is moving up
                if (endRow-startRow == 1  || ((pieceType == Type.KING) && (endRow-startRow == -1))) { //Check if move up 1 space
                    return false;
                } else if (isValidJump(move)) { //Check if skipping a piece
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
     * @return boolean
     */
    public boolean isValidJump(Move move) {
        int skipPieceRow, skipPieceCell = 0;
        Color playerColor;
        Type pieceType;
        int movesSize = this.moves.size();

        //-----------------------------------------------------------------
        // COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = this.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
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
                if (this.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece() != null) { //Check if piece exists
                    if (this.getRow(skipPieceRow).getSpace(skipPieceCell).getPiece().getColor() == otherPlayer) { //Check if piece is opposite color
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
     * @return boolean
     */
    public boolean newMoveExists() {
        if (this.moves.size() > 0) {
            if (!isValidJump(this.seeTopMove())) {
                return false;
            }
        }
        Move move = seeTopMove();
        //-----------------------------------------------------------------
        // COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        Color playerColor;
        Type pieceType;
        int movesSize = this.moves.size();
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.getRow(startRowI).getSpace(startCellI).getPiece().getColor();//Get player colors
            pieceType = this.getRow(startRowI).getSpace(startCellI).getPiece().getType();
        } else {
            playerColor = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getColor();
            pieceType = this.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece().getType();
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
        int endRow = this.seeTopMove().getEnd().getRow();
        int endCell = this.seeTopMove().getEnd().getCell();
        int startRow = this.seeTopMove().getStart().getRow();
        int startCell = this.seeTopMove().getStart().getCell();
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Begin checking for an available move
        //-----------------------------------------------------------------
        if(playerColor == Color.WHITE || pieceType == Type.KING) { // Check if its a White player
            if(endCell < 6) { //Check out of bounds
                if (endRow < 6) { //Check out of bounds
                    if (this.getRow(endRow + 1).getSpace(endCell + 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                        if (this.getRow(endRow + 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                            if ((this.getRow(endRow + 2).getSpace(endCell + 2).getPiece() == null) && ((endRow + 2 != startRow) || (endCell + 2 != startCell))) { //Check the space is free to jump
                                return true;
                            }
                        }
                    }
                }
            }
            if(endCell > 1) { //Check out of bounds
                if (endRow < 6) { //Check out of bounds
                    if (this.getRow(endRow + 1).getSpace(endCell - 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                        if (this.getRow(endRow + 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                            if ((this.getRow(endRow + 2).getSpace(endCell - 2).getPiece() == null) && ((endRow + 2 != startRow) || (endCell - 2 != startCell))) { //Check the space is free to jump
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if (playerColor == Color.RED || pieceType == Type.KING){
            if((endCell > 1) && (endRow > 1)) { //Check out of bounds
                if (this.getRow(endRow - 1).getSpace(endCell - 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                    if (this.getRow(endRow - 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) { // Check the the space that is being jumped is the other player
                        if ((this.getRow(endRow - 2).getSpace(endCell - 2).getPiece() == null) && ((endRow - 2 != startRow) || (endCell - 2 != startCell))) { //Check the space is free to jump
                            return true;
                        }
                    }
                }
            }
            if((endRow > 1) && (endCell < 6)) { //Check out of bounds
                if (this.getRow(endRow - 1).getSpace(endCell + 1).getPiece() != null) { // Check the the space that is being jumped is occupied
                    if (this.getRow(endRow - 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                        if ((this.getRow(endRow - 2).getSpace(endCell + 2).getPiece() == null) && ((endRow - 2 != startRow) || (endCell + 2 != startCell))) { //Check the space is free to jump
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return the flipped BoardView
     * @return a BoardView Instance
     */
    public BoardView flip()
    {
        ArrayList<Row> temp = new ArrayList<>();
        for (int i = 7; i >= 0; i--){
            temp.add(this.rows.get(i).flip());
        }
        return new BoardView(temp, this.moves);
    }

    /**
     * Adding a move to the start of list of moves
     * @param move the move to be added
     */
    public void addMove(Move move) {
        this.moves.add(0, move);
    }

    /**
     * Removing the move at index 0 from list
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

    /**
     * The board is in Win condition if there is only 1 color remaining
     * @return true if it is in the winning condition
     */
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

    /**
     * A string representation of the BoardView(aka board)
     *   +---+---+---+
     * 1 |   |   |   |
     *   +---+---+---+
     * @return the string representation
     */
    @Override
    public String toString() {
        String res = "";
        String border = border_s(rows.size());
        if (rows.size() > 0){
            res = res.concat("  ");
            for(Space space : rows.get(0).getSpaces()){
                res = res.concat("  "+space.getCellIdx()+" ");
            }
        }
        res = res.concat("\n" + border);
        for(Row row :rows){
            res = res.concat("\n" + row.toString() + "\n" + border);
        }
        return res;
    }

    /**
     * A helper function for toString() that prints out the border for the board:
     * border_s(5) ->  +---+---+---+---+---+
     * border_s(0) ->
     * border_s(1) ->  +---+
     * @param col the number of columns
     * @return the String representation
     */
    private String border_s(int col) {
        if (col == 0) {
            return "";
        } else {
            String res = "  +";
            for (int i = 0; i < col; i++) {
                res = res.concat("---+");
            }
            return res;
        }
    }
}
