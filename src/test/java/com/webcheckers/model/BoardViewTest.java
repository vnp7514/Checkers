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

