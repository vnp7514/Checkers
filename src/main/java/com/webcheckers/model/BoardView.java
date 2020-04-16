package com.webcheckers.model;

import java.util.*;

import com.webcheckers.util.Move;
import com.webcheckers.util.Position;


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

    /* The stack that contains the positions of spaces needed to be removed*/
    private Stack<Position> toBeRemoveList = new Stack<>();

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
        if (piece != null){
            Color color = piece.getColor();
            Type type = piece.getType();
            if (color == Color.RED && type == Type.SINGLE && row == 0){
                piece = new Piece(Type.KING, color);
            } else if (color == Color.WHITE && type == Type.SINGLE && row ==7){
                piece = new Piece(Type.KING, color);
            }
        }
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
     * This will modify the game board according to the list of positions to remove
     * It will remove any piece at each of the position in the stack.
     * It will empty the stack
     */
    private void popToBeRemovedStack(){
        while (!toBeRemoveList.isEmpty()){
            Position p = toBeRemoveList.pop();
            setPiece(p.getRow(), p.getCell(), null);
        }
    }

    /**
     * Remove the recent Position added to the stack if its associated move has
     *    been removed
     * @param row  the row index of the position
     * @param col  the col index of the position
     */
    private void revertToBeRemovedStack(int row, int col){
        if (!toBeRemoveList.isEmpty()){
            Position p = toBeRemoveList.peek();
            if (p.getRow() == row && p.getCell() == col){
                toBeRemoveList.pop();
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
        return this.getRow(rowidx).getSpace(cellidx).isValid();
    }

    /**
     * Handles the movements of the piece based on the stored moves in the moves array list(Part of BoardView)
     */
    public void movePiece(){
        removeAllMoves();
        popToBeRemovedStack();
       /** int movesSize = this.moves.size();
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
        */
    }

    /**
     * Check if the players move was a valid move to make
     *  - Checks that the player is going the right way
     *  - Checks the player is going diagonally
     *  - Checks to make sure jumps are valid jumps
     * @param move The move that the player made
     * @return true if the move is valid
     */
    public boolean isValidMove(Move move){
        if (viewPiece(move.getStart().getRow(), move.getStart().getCell()) == null){
            return false; // if the player moves a piece from an empty space
        }
        if (viewPiece(move.getEnd().getRow(),move.getEnd().getCell())!= null){
            return false; // if the player moves to an occupied space
        }
        int movesSize = this.moves.size();
        if (movesSize > 0){ // if there were moves made, this new move has to start from there
            if (!seeTopMove().getEnd().equals(move.getStart())){
                return false;
            }
        }
        Color playerColor;
        Type pieceType;
        //-----------------------------------------------------------------
        //  COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.viewPiece(startRowI, startCellI).getColor();//Get player colors
            pieceType = this.viewPiece(startRowI,startCellI).getType();
        } else {
            playerColor = viewPiece(move.getStart().getRow(),move.getStart().getCell()).getColor();
            pieceType = this.viewPiece(move.getStart().getRow(),move.getStart().getCell()).getType();
        }
        //-----------------------------------------------------------------

        //-----------------------------------------------------------------
        // Get start and end rows
        //-----------------------------------------------------------------
        int startRow = move.getStart().getRow();
        int endRow = move.getEnd().getRow();
        int endCol = move.getEnd().getCell();
        int startCol = move.getStart().getCell();
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------
        // Begin validation
        //-----------------------------------------------------------------
        if (this.seeTopMove() == null) { // Check if its the first move
            if (playerColor == Color.WHITE) {
                if ( (endCol - startCol == 1 || endCol - startCol == -1) &&
                        (endRow == startRow+1 ||
                                (pieceType == Type.KING && startRow-1 == endRow)) ) {
                    //Check that player is moving up or if player is king, moving down too
                    // Then make sure that the player doesnt go too far left or right
                    return true;
                } else return isValidJump(move); //Check if skipping a piece

            } else { // Player is Red
                if ((endCol - startCol == 1 || startCol -1 == endCol) &&
                        (endRow == startRow-1 ||
                                (pieceType == Type.KING && startRow+1==endRow))){
                    return true;
                } else return isValidJump(move);
            }

        } else if (isValidJump(this.seeTopMove())) { // Perhaps a second jump?
            if ((endCol - startCol == 1 || endCol - startCol == -1)||
                    (endRow - startRow ==1 || startRow -1 == endRow)) {
                    //Check that player is moving 1 space to any direction
                    // Return false because this is supposed to be a second jump
                    return false;
                } else return (isValidJump(move)); //Check if skipping a piece
        }
        return false;
        //-----------------------------------------------------------------
    }

    /**
     * Check if the move made by the player is a valid jump move
     * Pre-condition: the Position end is a valid space to move to
     * @param move The move that the player
     * @return boolean
     */
    public boolean isValidJump(Move move) {
        if (viewPiece(move.getEnd().getRow(), move.getEnd().getCell()) != null){
            // if the space the piece moving to is occupied then it shouldnt be allowed
            return false;
        }
        if (move.getEnd().getCell() - move.getStart().getCell() == 1 ||
        move.getEnd().getCell() -move.getStart().getCell() ==-1 ||
        move.getEnd().getRow() - move.getStart().getRow() ==-1 ||
        move.getEnd().getRow() - move.getStart().getRow() ==1){ // if this is a single move
            return false;
        }
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

            playerColor = this.viewPiece(startRowI,
                    startCellI).getColor();//Get player colors
            pieceType = this.viewPiece(startRowI,startCellI).getType();
        } else {
            playerColor = this.viewPiece(move.getStart().getRow(),
                    move.getStart().getCell()).getColor();
            pieceType = this.viewPiece(move.getStart().getRow(),
                    move.getStart().getCell()).getType();
        }
        //-----------------------------------------------------------------


        //-----------------------------------------------------------------

        int startRow = move.getStart().getRow();
        int endRow = move.getEnd().getRow();
        int startCol = move.getStart().getCell();
        int endCol = move.getEnd().getCell();


        //-----------------------------------------------------------------

        //-----------------------------------------------------------------
        // Check if valid jump
        //-----------------------------------------------------------------
        if (playerColor == Color.WHITE) {
            if (endRow - startRow == 2 || ((pieceType == Type.KING)
                    && (endRow - startRow == -2)) &&
                    (endCol - startCol == 2 || endCol - startCol == -2)) {
                // check if the player moves up 2 rows as a single or 2 rows down for a king too
                // check if the player moves right or left 2 cols
                skipPieceRow = (startRow + endRow) / 2; //Set skipped piece row
                skipPieceCell = (startCol + endCol) / 2; //Set skipped piece cell
                Piece middle_piece = viewPiece(skipPieceRow,skipPieceCell);
                if (middle_piece != null) { //Check if piece exists
                    if (middle_piece.getColor() == Color.RED) { //Check if piece is opposite color
                        return true;
                    }
                }
            }
        } else { //red Player
            if (endRow - startRow == -2 || ((pieceType == Type.KING)
                    && (endRow - startRow == 2)) &&
                    (endCol - startCol == 2 || endCol - startCol == -2)) {
                // check if the player moves down 2 rows as a single or 2 rows up for a king too
                // check if the player moves right or left 2 cols
                skipPieceRow = (endRow + startRow ) / 2; //Set skipped piece row
                skipPieceCell = (endCol + startCol) / 2; //Set skipped piece cell
                Piece piece = viewPiece(skipPieceRow,skipPieceCell);
                if (piece != null) { //Check if piece exists
                    if (piece.getColor() == Color.WHITE) { //Check if piece is opposite color
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
     * @return boolean true if there is
     */
    public boolean newJumpExists(Color playerColor) {
        /**if (this.moves.size() > 0) {
            if (!isValidJump(this.seeTopMove())) {
                return false;
            }
        }
        Move move = seeTopMove();
        //-----------------------------------------------------------------
        // COLOR AND TYPE FINDER
        //-----------------------------------------------------------------
        Type pieceType;
        int movesSize = this.moves.size();
        if (movesSize >= 1) {
            int startRowI = this.moves.get(movesSize - 1).getStart().getRow();
            int startCellI = this.moves.get(movesSize - 1).getStart().getCell();

            playerColor = this.viewPiece(startRowI,
                    startCellI).getColor();//Get player colors
            pieceType = this.viewPiece(startRowI,startCellI).getType();
        } else {
            playerColor = this.viewPiece(move.getStart().getRow(),
                    move.getStart().getCell()).getColor();
            pieceType = this.viewPiece(move.getStart().getRow(),
                    move.getStart().getCell()).getType();
        }
        //-----------------------------------------------------------------
*/
        //-----------------------------------------------------------------
        // Get other player color
        //-----------------------------------------------------------------
        if (moves.size() > 0){
            Move move = seeTopMove();
            if (!isValidJump(move)){ // If the player only did a single
                // move then no need to check for more jumps
                return false;
            }
            int endRow = move.getEnd().getRow();
            int endCol = move.getEnd().getCell();
            Position current = new Position(endRow,endCol);
            Position upperleft = new Position(endRow-2, endCol-2);
            Position upperright = new Position(endRow-2, endCol+2);
            Position lowerleft = new Position(endRow+2, endCol-2);
            Position lowerright = new Position(endRow+2, endCol+2);
            // Check all possible jumps
            if (endRow-2>=0&&endCol-2>=0&&
                    viewPiece(endRow-2,endCol-2) == null &&
                    isValidJump(new Move(current,upperleft))){
                return true;
            } else if (endRow-2>=0&&endCol+2<=7&&
                    viewPiece(endRow-2,endCol+2) == null &&
                    isValidJump(new Move(current,upperright))){
                return true;
            } else if (endRow+2<=7&&endCol-2>=0&&
                    viewPiece(endRow+2,endCol-2) == null &&
                    isValidJump(new Move(current,lowerleft))){
                return true;
            } else return endRow+2<=7&&endCol+2<=7&&
                    viewPiece(endRow+2,endCol+2) == null &&
                    isValidJump(new Move(current,lowerright));
        }
        // else statement
        Color otherPlayer;
        if (playerColor == Color.WHITE) {
            otherPlayer = Color.RED;
        } else {
            otherPlayer = Color.WHITE;
        }
        //-----------------------------------------------------------------

        for (int i = 0; i < 8; i++ ) {
            Row r = getRow(i);
            for (Space s : r.getSpaces()) {
                //-----------------------------------------------------------------
                // Get start and end positions
                //-----------------------------------------------------------------
                if (s.getPiece() != null) {
                    if (s.getPiece().getColor() == playerColor) {
                        int startRow = i;
                        int startCell = s.getCellIdx();
                        Position current = new Position(startRow, startCell);
                        Position upperleft = new Position(startRow-2, startCell-2);
                        Position upperright = new Position(startRow-2, startCell+2);
                        Position lowerleft = new Position(startRow+2, startCell-2);
                        Position lowerright = new Position(startRow+2, startCell+2);
                        if (startRow-2>=0&&startCell-2>=0&&
                                viewPiece(startRow-2,startCell-2) == null &&
                                isValidJump(new Move(current,upperleft))){
                            return true;
                        } else if (startRow-2>=0&&startCell+2<=7&&
                                viewPiece(startRow-2,startCell-2) == null &&
                                isValidJump(new Move(current,upperright))){
                            return true;
                        } else if (startRow+2<=7&&startCell-2>=0&&
                                viewPiece(startRow+2,startCell-2) == null &&
                                isValidJump(new Move(current,lowerleft))){
                            return true;
                        } else if(startRow+2<=7&&startCell+2<=7&&
                                viewPiece(startRow+2, startCell+2) == null &&
                                isValidJump(new Move(current,lowerright))){
                            return true;
                        }
                        //-----------------------------------------------------------------
                        /**
                        //-----------------------------------------------------------------
                        // Begin checking for an available move
                        //-----------------------------------------------------------------
                        if (playerColor == Color.WHITE || pieceType == Type.KING) { // Check if its a White player
                            if (endCell < 6) { //Check out of bounds
                                if (endRow < 6) { //Check out of bounds
                                    if (this.viewPiece(endRow + 1, endCell + 1)
                                            != null) { // Check the the space that is being jumped is occupied
                                        if (this.viewPiece(endRow + 1, endCell + 1).getColor()
                                                == otherPlayer) { // Check the the space that is being jumped is the other player
                                            if ((this.viewPiece(endRow + 2, endCell + 2) == null)
                                                    && ((endRow + 2 != startRow) ||
                                                    (endCell + 2 != startCell))) { //Check the space is free to jump
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (endCell > 1) { //Check out of bounds
                                if (endRow < 6) { //Check out of bounds
                                    if (this.viewPiece(endRow + 1, endCell - 1) != null) { // Check the the space that is being jumped is occupied
                                        if (this.viewPiece(endRow + 1, endCell - 1).getColor()
                                                == otherPlayer) { // Check the the space that is being jumped is the other player
                                            if ((this.viewPiece(endRow + 2, endCell - 2) == null)
                                                    && ((endRow + 2 != startRow)
                                                    || (endCell - 2 != startCell))) { //Check the space is free to jump
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (playerColor == Color.RED || pieceType == Type.KING) {
                            if ((endCell > 1) && (endRow > 1)) { //Check out of bounds
                                if (this.viewPiece(endRow - 1, endCell - 1) != null) { // Check the the space that is being jumped is occupied
                                    if (this.viewPiece(endRow - 1, endCell - 1).getColor()
                                            == otherPlayer) { // Check the the space that is being jumped is the other player
                                        if ((this.viewPiece(endRow - 2, endCell - 2) == null)
                                                && ((endRow - 2 != startRow) ||
                                                (endCell - 2 != startCell))) { //Check the space is free to jump
                                            return true;
                                        }
                                    }
                                }
                            }
                            if ((endRow > 1) && (endCell < 6)) { //Check out of bounds
                                if (this.viewPiece(endRow - 1, endCell + 1) != null) { // Check the the space that is being jumped is occupied
                                    if (this.viewPiece(endRow - 1, endCell + 1).getColor()
                                            == otherPlayer) {
                                        if ((this.viewPiece(endRow - 2, endCell + 2) == null)
                                                && ((endRow - 2 != startRow)
                                                || (endCell + 2 != startCell))) { //Check the space is free to jump
                                            return true;
                                        }
                                    }
                                }
                            }
                        } */
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check whether there are more moves to be made by this player
     * @param playerColor the color of the current player
     * @return true if the player can make more moves
     */
    public boolean newMoveExists(Color playerColor){
        if (moves.isEmpty()) {
            // The first move of the player
            if (newJumpExists(playerColor)) {
                return true;
            }
            for (int i = 0; i < 8; i++) {
                Row r = getRow(i);
                for (Space s : r.getSpaces()) {
                    if (s.getPiece() != null &&
                            s.getPiece().getColor() == playerColor){
                        int startRow = i;
                        int startCell = s.getCellIdx();
                        Position current = new Position(startRow , startCell);
                        Position upperleft = new Position(startRow-1 ,
                                startCell-1);
                        Position lowerleft = new Position(startRow+1,
                                startCell-1);
                        Position upperright = new Position(startRow-1 ,
                                startCell+1);
                        Position lowerright = new Position(startRow+1,
                                startCell+1);
                        if (startRow-1>=0&&startCell-1>=0&&
                                viewPiece(startRow-1,startCell-1) == null &&
                                isValidMove(new Move(current,upperleft))){
                            return true;
                        } else if (startRow-1>=0&&startCell+1<=7&&
                                viewPiece(startRow-1,startCell-1) == null &&
                                isValidMove(new Move(current,upperright))){
                            return true;
                        } else if (startRow+1<=7&&startCell-1>=0&&
                                viewPiece(startRow+1,startCell-1) == null &&
                                isValidMove(new Move(current,lowerleft))){
                            return true;
                        } else if(startRow+1<=7&&startCell+1<=7&&
                                viewPiece(startRow+1, startCell+1) == null &&
                                isValidMove(new Move(current,lowerright))){
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
        int endCol = move.getEnd().getCell();
        int startCol = move.getStart().getCell();
        int startRow = move.getStart().getRow();
        int endRow = move.getEnd().getRow();
        if (endCol == startCol +2 || endCol ==startCol-2){
            int skipRow = (startRow+endRow)/2;
            int skilCol = (startCol+endCol)/2;
            toBeRemoveList.push(new Position(skipRow,skilCol));
        }
    }

    /**
     * Removing the move at index 0 from list as well as the position of the removed
     *  piece stored in the stack that is associated with this move
     */
    public Move removeMove() {
        if (this.moves.isEmpty()){
            return null;
        }
        Move move = this.moves.remove(0);
        if (move.getStart().getCell() - move.getEnd().getCell() == 2 ||
                move.getStart().getCell() - move.getEnd().getCell() == -2) {
            int skipPieceRow = (move.getEnd().getRow()
                    + move.getStart().getRow()) / 2; //Set skipped piece row
            int skipPieceCell = (move.getEnd().getCell()
                    + move.getStart().getCell()) / 2;
            revertToBeRemovedStack(skipPieceRow, skipPieceCell);
        }
        return move;
    }

    /**
     * Return the most recent move
     * @return null if moves is empty. Otherwise, the top element
     */
    private Move seeTopMove() {
        if (this.moves.isEmpty()) {
            return null;
        } else {
            return this.moves.get(0);
        }
    }

    /**
     * Remove all the moves from the list of moves and update the board according
     * to the moves removed
     */
    private void removeAllMoves() {
        Move move = seeTopMove();
        Move first = moves.get(moves.size()-1);
        Piece piece = viewPiece(first.getStart().getRow(), first.getStart().getCell());
        setPiece(move.getEnd().getRow(),move.getEnd().getCell(), piece);
        while (move != null) {
            setPiece(move.getStart().getRow(), move.getStart().getCell(), null);
            this.moves.remove(move);
            move = seeTopMove();
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
