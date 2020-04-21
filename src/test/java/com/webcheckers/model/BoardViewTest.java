package com.webcheckers.model;

import com.webcheckers.util.Move;
import com.webcheckers.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Tag("Model-tier")
public class BoardViewTest {

    private BoardView boardView1;

    private Piece singleRedPiece = new Piece(Type.SINGLE, Color.RED);
    private Piece singleWhitePiece = new Piece(Type.SINGLE, Color.WHITE);
    private Piece kingRedPiece = new Piece(Type.KING, Color.RED);
    private Piece kingWhitePiece = new Piece(Type.KING, Color.WHITE);


    @BeforeEach
    public void setup(){
        boardView1 = new BoardView();
    }

    @Test
    public void flippedBoard(){
        System.out.println("Visually verify: ");
        BoardView flip = boardView1.flip();
        assertEquals(flip.viewPiece(0,0),
                boardView1.viewPiece(0,0));
        assertEquals(flip.viewPiece(7,6),
                boardView1.viewPiece(7,6));
        assertEquals(flip.viewPiece(1,0),
                boardView1.viewPiece(1,0));
        print_helper(boardView1);
        assertNull(boardView1.viewPiece(0,0));
        assertEquals(singleWhitePiece, boardView1.viewPiece(0,1));
        assertNull(flip.viewPiece(0,0));
        assertEquals(singleWhitePiece, flip.viewPiece(0,1));
        BoardView test = BoardView.testBoard();
        print_helper(test);
    }

    @Test
    public void isValidTest(){
        BoardView flip = boardView1.flip();
        assertFalse(boardView1.isValid(0,0));
        assertFalse(boardView1.isValid(2, 1));
        assertFalse(boardView1.isValid(3,1));
        assertTrue(boardView1.isValid(3,0));
        assertTrue(boardView1.isValid(4, 1));
        assertFalse(flip.isValid(0,0));
        assertFalse(flip.isValid(2, 1));
        assertFalse(flip.isValid(3,1));
        assertTrue(flip.isValid(3,0));
        assertTrue(flip.isValid(4, 1));
    }

    /**
     * Look at the original board to understand the positions of the cells
     */
    @Test
    public void isValidMoveTest(){
          Position p11 = new Position(1,1);
          Position p12 = new Position(1,2);
          Position p21 = new Position(2,1);
          Position p10 = new Position(1,0);
          Position p20 = new Position(2,0);
          Position p22 = new Position(2,2);
          Position p31 = new Position(3,1);
          Position p32 = new Position(3,2);
          Position p30 = new Position(3,0);
          Position p56 = new Position(5,6);
          Position p45 = new Position(4,5);
          Position p47 = new Position(4,7);
          Position p50 = new Position(5,0);
          Position p54 = new Position(5,4);
          Position p14 = new Position(1,4);
          BoardView flip = boardView1.flip();

          // Testing single moves
          assertFalse(boardView1.isValidMove(new Move(p11, p12)));
          assertFalse(boardView1.isValidMove(new Move(p10, p21)));
          assertFalse(boardView1.isValidMove(new Move(p21,p11)));
          assertFalse(boardView1.isValidMove(new Move(p21,p20)));
          assertFalse(boardView1.isValidMove(new Move(p21,p22)));
          assertFalse(boardView1.isValidMove(new Move(p21,p31)));
          assertTrue(boardView1.isValidMove(new Move(p21,p32)));
          assertTrue(boardView1.isValidMove(new Move(p21,p30)));
          assertFalse(boardView1.isValidMove(new Move(p32,p21)));
          assertFalse(boardView1.isValidMove(new Move(p30,p21)));
          assertTrue(boardView1.isValidMove(new Move(p56,p45)));
          assertTrue(boardView1.isValidMove(new Move(p56,p47)));
        assertFalse(flip.isValidMove(new Move(p11, p12)));
        assertFalse(flip.isValidMove(new Move(p10, p21)));
        assertFalse(flip.isValidMove(new Move(p21,p11)));
        assertFalse(flip.isValidMove(new Move(p21,p20)));
        assertFalse(flip.isValidMove(new Move(p21,p22)));
        assertFalse(flip.isValidMove(new Move(p21,p31)));
        assertTrue(flip.isValidMove(new Move(p21,p32)));
        assertTrue(flip.isValidMove(new Move(p21,p30)));
        assertFalse(flip.isValidMove(new Move(p32,p21)));
        assertFalse(flip.isValidMove(new Move(p30,p21)));
        assertTrue(flip.isValidMove(new Move(p56,p45)));
        assertTrue(flip.isValidMove(new Move(p56,p47)));
        boardView1.setPiece(2,1, null);
        boardView1.setPiece(3,2,singleWhitePiece);
        boardView1.setPiece(3,0,kingWhitePiece);
        print_helper(boardView1);
        assertFalse(boardView1.isValidMove(new Move(p32,p21)));
        assertTrue(boardView1.isValidMove(new Move(p30,p21)));
        assertFalse(flip.isValidMove(new Move(p32,p21)));
        assertTrue(flip.isValidMove(new Move(p30,p21)));

        BoardView test = BoardView.testBoard();
        test.setPiece(3,2,singleWhitePiece);
        test.setPiece(4,3,singleRedPiece);
        test.setPiece(4,1,singleRedPiece);
        test.setPiece(2,1,singleRedPiece);
        test.setPiece(2,3,singleRedPiece);
        print_helper(test);
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(5,4))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(5,0))));
        assertFalse(test.isValidMove(new Move(new Position(3,2),
                new Position(1,0))));
        assertFalse(test.isValidMove(new Move(new Position(3,2),
                new Position(1,4))));

        test = BoardView.testBoard();
        test.setPiece(3,2,singleRedPiece);
        test.setPiece(4,3,singleWhitePiece);
        test.setPiece(4,1,singleWhitePiece);
        test.setPiece(2,1,singleWhitePiece);
        test.setPiece(2,3,singleWhitePiece);
        print_helper(test);
        assertFalse(test.isValidMove(new Move(new Position(3,2),
                new Position(5,4))));
        assertFalse(test.isValidMove(new Move(new Position(3,2),
                new Position(5,0))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(1,0))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(1,4))));

        test = BoardView.testBoard();
        test.setPiece(3,2,kingRedPiece);
        test.setPiece(4,3,singleWhitePiece);
        test.setPiece(4,1,singleWhitePiece);
        test.setPiece(2,1,kingWhitePiece);
        test.setPiece(2,3,singleWhitePiece);
        print_helper(test);
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(5,4))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(5,0))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(1,0))));
        assertTrue(test.isValidMove(new Move(new Position(3,2),
                new Position(1,4))));


        test = BoardView.testBoard();
        test.setPiece(3,2, kingWhitePiece);
        test.setPiece(2,1,kingRedPiece);
        test.setPiece(2,3, kingRedPiece);
        test.setPiece(4,1 , kingRedPiece);
        test.setPiece(1,4, kingRedPiece);
        printb(test);
        assertFalse(test.isValidMove(new Move(p32, p14)));
        assertTrue(test.isValidMove(new Move (p32,p50)));
        assertTrue(test.isValidMove(new Move(p32,p10)));
        assertFalse(test.isValidMove(new Move(p32, p54)));
        assertTrue(test.isValidMove(new Move(p21,new Position(4,3))));
        assertFalse(test.isValidMove(new Move(p14, p32)));
        assertFalse(test.isValidMove(new Move(new Position(2,3),
                new Position(4,1))));
    }

    @Test
    public void movePieceTest(){
        boardView1.addMove(new Move(new Position(2,1), new Position(3,2)));
        printb(boardView1);
        System.out.println("Moving row2cell1 to row3cell2");
        boardView1.movePiece();
        printb(boardView1);
        boardView1.addMove(new Move(new Position(5,2),
                new Position(4,3)));
        boardView1.movePiece();
        System.out.println("Moving row5cell2 to row4cell3");
        printb(boardView1);
        boardView1.addMove(new Move(new Position(2,3),
                new Position(3,4)));
        boardView1.movePiece();
        System.out.println("Moving row2cell3 to row3cell4");
        printb(boardView1);
        boardView1.addMove(new Move(new Position(4,3), new Position(2,1)));
        boardView1.movePiece();
        System.out.println("Moving row4cell3 to row2cell1");
        printb(boardView1);

        BoardView test = BoardView.testBoard();
        test.setPiece(4,3,singleRedPiece);
        test.setPiece(3,2, singleWhitePiece);
        test.setPiece(1,2,singleWhitePiece);
        test.addMove(new Move (new Position(4,3),
                new Position(2,1)));
        test.addMove(new Move( new Position(2,1),
                new Position(0,3) ));
        printb(test);
        test.movePiece();
        System.out.println("Doing a double jump");
        printb(test);
    }

    @Test
    public void newJumpExists(){
        // Check if there is a jump available
        assertFalse(boardView1.newJumpExists(Color.WHITE));
        assertFalse(boardView1.newJumpExists(Color.RED));
        boardView1.setPiece(4,1,singleWhitePiece);
        printb(boardView1);
        assertTrue(boardView1.newJumpExists(Color.RED));
        assertFalse(boardView1.newJumpExists(Color.WHITE));
        BoardView test = BoardView.testBoard();

        // If the piece is adjacent to no pieces
        test.setPiece(3,2, kingRedPiece);
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(3,2,kingRedPiece);

        // If upperright piece is of different color
        test.setPiece(2,3,singleWhitePiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,kingRedPiece);
        test.setPiece(2,3,null);

        // If upperleft piece is of different color
        test.setPiece(2,1,singleWhitePiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,kingRedPiece);

        // If upperleft piece is of same color
        test.setPiece(2,1,singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(3,2,kingRedPiece);
        test.setPiece(2,1, null);

        // If lower left piece is of different color
        test.setPiece(4,1,kingWhitePiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED)); // single piece cannot go backward
        test.setPiece(3,2,kingRedPiece);
        test.setPiece(4,1, null);

        test.setPiece(4,3, singleWhitePiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(3,2,singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED)); // single piece cannot go backward
        test.setPiece(3,2,kingRedPiece);
        test.setPiece(4,3, null);

        test = BoardView.testBoard();
        // Testing corners
        // upper right corner
        test.setPiece(0,7,singleWhitePiece);
        assertFalse(test.newJumpExists(Color.WHITE));
        test.setPiece(1,6,singleRedPiece);
        assertTrue(test.newJumpExists(Color.WHITE));
        test.setPiece(0,7,null);
        test.setPiece(1,6, null);

        //lower left corner
        test.setPiece(7,0, singleRedPiece);
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(6,1,singleWhitePiece);
        assertTrue(test.newJumpExists(Color.RED));
        test.setPiece(7,0, null);
        test.setPiece(6,1,null);

        // upper left corner
        test.setPiece(0,1,singleWhitePiece);
        test.setPiece(1,0,singleRedPiece);
        assertFalse(test.newJumpExists(Color.WHITE));
        assertFalse(test.newJumpExists(Color.RED));
        test.setPiece(0,1, null);
        test.setPiece(1,0, null);


        // lower right corner
        test.setPiece(6,7,singleWhitePiece);
        test.setPiece(7,6,singleRedPiece);
        assertFalse(test.newJumpExists(Color.WHITE));
        assertFalse(test.newJumpExists(Color.RED));
    }

    @Test
    public void newMoveExists(){
        assertTrue(boardView1.newMoveExists(Color.RED));
        assertTrue(boardView1.newMoveExists(Color.WHITE));
        boardView1.setPiece(3,0, singleRedPiece);
        boardView1.setPiece(3,2,singleRedPiece);
        boardView1.setPiece(3,4,singleRedPiece);
        boardView1.setPiece(3,6,singleRedPiece);
        boardView1.setPiece(4,1,singleRedPiece);
        boardView1.setPiece(4,3,singleRedPiece);
        boardView1.setPiece(4,5,singleRedPiece);
        assertTrue(boardView1.newMoveExists(Color.WHITE));
        assertTrue(boardView1.newMoveExists(Color.RED));
        boardView1.setPiece(4,7,singleRedPiece);
        // boardview is at a state where no pieces could move
        assertFalse(boardView1.newMoveExists(Color.WHITE));
        assertFalse(boardView1.newMoveExists(Color.RED));

    }

    @Test
    public void winCondititionTest(){
        BoardView test = BoardView.testBoard();
        test.setPiece(0,1, singleRedPiece);
        print_helper(test);
        assertTrue(test.winCondition());
        test.setPiece(1,0, singleWhitePiece);
        print_helper(test);
        assertFalse(test.winCondition());
    }

    @Test
    public void removeMoveTest(){
        assertNull(boardView1.removeMove());
        boardView1.addMove(new Move(new Position(1,2), new Position(2,3)));
        assertNotNull(boardView1.removeMove());
        boardView1.addMove((new Move(new Position(1,2), new Position(3, 4))));
        assertNotNull(boardView1.removeMove());
    }

    /**
     * Help to print out both the original board and the flipped board view
     */
    public void print_helper(BoardView board){
        BoardView flip = board.flip();
        System.out.println("Origin:\n"+board.toString()+"\n");
        System.out.println("Flipped:\n"+flip.toString());
    }

    public void printb(BoardView board){
        System.out.println("Origin:\n"+board.toString()+"\n");

    }

}

