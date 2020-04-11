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
     * List of Rows of Spaces
     */
    private ArrayList<Row> rows;

    /**
     * The list of made moves
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
     * Returns the Iterator of the Arraylist of Rows
     * @return An Iterator of the Rows
     */
    @Override
    public Iterator<Row> iterator() {
        return this.rows.iterator();
    }

    /**
     * Get the row from the Row list
     * @param indx the idx of the row
     * @return the Row instance
     */
    public Row getRow(int indx) {
        return this.rows.get(indx);
    }

    /**
     * Put a piece on the space at the specific row idx and col idx
     * @param row the row idx
     * @param col the col idx
     * @param piece the piece
     */
    public void setPiece(int row, int col, Piece piece) {
        this.rows.get(row).setSpace(col, piece);
    }

    /**
     * View the piece at this space
     * @param row the row idx
     * @param col the col idx
     * @return the piece
     */
    public Piece viewPiece(int row,int col) {
        return this.rows.get(row).viewPiece(col);
    }

    /**
     * Modify the board according to the move
     */
    public void movePiece(){
        Move move = seeTopMove();
        int movesSize = this.moves.size();

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

        for (Move m : this.moves) {
            int endRow = m.getEnd().getRow();
            int endCell = m.getEnd().getCell();
            int startRow = m.getStart().getRow();
            int startCell = m.getStart().getCell();

            if (startRow < endRow) {//White
                if (endRow-startRow == 1) {
                    if (endRow == 7) {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);
                } else if (endRow - startRow == 2) {
                    int skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                    int skipPieceCell = (startRow + endRow) / 2; //Set skipped piece cell
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);
                    this.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null);
                    if (m.equals(this.seeTopMove())) {
                        if (endRow == 7) {
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                        } else {
                            this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                        }
                    }

                }
            } else if (endRow < startRow) { //Red
                if (startRow-endRow == 1) {
                    if (endRow == 0) {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(Type.KING,playerColor));
                    } else {
                        this.rows.get(endRow).getSpace(endCell).setPiece(new Piece(pieceType,playerColor));
                    }
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);
                } else if (startRow - endRow == 2) {
                    int skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                    int skipPieceCell = (startCell + endCell) / 2; //Set skipped piece cell
                    this.rows.get(startRow).getSpace(startCell).setPiece(null);
                    this.rows.get(skipPieceRow).getSpace(skipPieceCell).setPiece(null);
                    if (m.equals(this.seeTopMove())) {
                        if (endRow == 0) {
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
     * Check whether the space at rowidx and cellidx (colidx) is
     *    a valid space (aka. a black title and does not hold a piece)
     * @param rowidx the row index
     * @param cellidx the cell index
     * @return true if it is valid. false otherwise
     */
    public boolean isValid(int rowidx, int cellidx){
        return getRow(rowidx).getSpace(cellidx).isValid();
    }

    /**
     * Check whether the move was a valid (legal) move
     * @param move the Move that contains the info of the move
     * @return true if it is valid, false otherwise
     */
    public boolean isValidMove(Move move){
        Color playerColor;
        int movesSize = this.moves.size();
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

        if (this.seeTopMove() == null) {
                    if(endRow > startRow || ((pieceType == Type.KING) && startRow > endRow)){ //Check that player is moving up
                        if (endRow-startRow == 1 || ((pieceType == Type.KING) && (endRow-startRow == -1))) { //Check if move up 1 space
                            if (move.getEnd().getCell()-move.getStart().getCell() == 1 || move.getEnd().getCell()-move.getStart().getCell() == -1) {
                                return true;
                            }
                        } else if (isValidJump(move)) { //Check if skipping a piece
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
    }

    /**
     * Check whether the Move is a valid jump according to Checkers Rule
     * @param move the Move that contains the info
     * @return true if it was, false otherwise
     */
    public boolean isValidJump(Move move) {
        int skipPieceRow, skipPieceCell = 0;
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

        if (endRow-startRow == 2 || ((pieceType == Type.KING)&&(endRow-startRow == -2))) {
            if (move.getEnd().getCell()-move.getStart().getCell() == 2 || move.getEnd().getCell()-move.getStart().getCell() == -2) {
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
    }

    /**
     * Return true if there are more moves to made
     * @return true if the above is true
     */
    public boolean newMoveExists() {
        Move move = seeTopMove();
        if (this.moves.size() > 0) {
            if (!isValidJump(this.seeTopMove())) {
                return false;
            }
        }

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

        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }

        int endRow = this.seeTopMove().getEnd().getRow();
        int endCell = this.seeTopMove().getEnd().getCell();
        int startRow = this.seeTopMove().getStart().getRow();
        int startCell = this.seeTopMove().getStart().getCell();

        if(playerColor == Color.WHITE || pieceType == Type.KING) {
            if(endCell < 6) { //Check out of bounds
                if (endRow < 6) {
                    if (this.getRow(endRow + 1).getSpace(endCell + 1).getPiece() != null) {
                        if (this.getRow(endRow + 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                            if ((this.getRow(endRow + 2).getSpace(endCell + 2).getPiece() == null) && ((endRow + 2 != startRow) && (endCell + 2 != startCell))) {
                                return true;
                            }
                        }
                    }
                }
            }
            if(endCell > 1) { //Check out of bounds
                if (endRow < 6) {
                    if (this.getRow(endRow + 1).getSpace(endCell - 1).getPiece() != null) {
                        if (this.getRow(endRow + 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) {
                            if (this.getRow(endRow + 2).getSpace(endCell - 2).getPiece() == null && ((endRow + 2 != startRow) && (endCell - 2 != startCell))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if (playerColor == Color.RED || pieceType == Type.KING){
            if((endCell > 1) && (endRow > 1)) { //Check out of bounds
                if (this.getRow(endRow - 1).getSpace(endCell - 1).getPiece() != null) {
                    if (this.getRow(endRow - 1).getSpace(endCell - 1).getPiece().getColor() == otherPlayer) {
                        if (this.getRow(endRow - 2).getSpace(endCell - 2).getPiece() == null && ((endRow - 2 != startRow) && (endCell - 2 != startCell))) {
                            return true;
                        }
                    }
                }
            }
            if((endRow > 1) && (endCell < 6)) { //Check out of bounds
                if (this.getRow(endRow - 1).getSpace(endCell + 1).getPiece() != null) {
                    if (this.getRow(endRow - 1).getSpace(endCell + 1).getPiece().getColor() == otherPlayer) {
                        if (this.getRow(endRow - 2).getSpace(endCell + 2).getPiece() == null && ((endRow - 2 != startRow) && (endCell + 2 != startCell))) {
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
    private String border_s(int col){
        if (col == 0){
            return "";
        } else {
            String res = "  +";
            for (int i = 0; i < col; i++){
                res = res.concat("---+");
            }
            return res;
        }
    }
}
