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
          BoardView flip = boardView1.flip();
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

    /**
     * Help to print out both the original board and the flipped board view
     */
    public void print_helper(BoardView board){
        BoardView flip = board.flip();
        System.out.println("Origin:\n"+board.toString()+"\n");
        System.out.println("Flipped:\n"+flip.toString());
    }

}

