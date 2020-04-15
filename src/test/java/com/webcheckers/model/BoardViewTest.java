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
        //assertEquals(flip.getRow(0).getSpace(0).getPiece(),
         //       boardView1.getRow(0).getSpace(0).getPiece());
        //assertEquals(flip.getRow(7).getSpace(6).getPiece(),
         //       boardView1.getRow(0).getSpace(1).getPiece());
        //assertEquals(flip.getRow(1).getSpace(0).getPiece(),
        //        boardView1.getRow(7).getSpace(0).getPiece());
        print_helper();
        assertNull(boardView1.viewPiece(0,0));
        assertEquals(singleWhitePiece, boardView1.viewPiece(0,1));
        assertNull(flip.viewPiece(0,0));
        assertEquals(singleWhitePiece, flip.viewPiece(0,1));
    }

    @Test
    public void isValidTest(){
        BoardView flip = boardView1.flip();
        assertFalse(boardView1.isValid(0,0));
        assertFalse(boardView1.isValid(2, 1));
        assertTrue(boardView1.isValid(3,0));
        assertTrue(boardView1.isValid(4, 1));
        assertFalse(flip.isValid(0,0));
        assertFalse(flip.isValid(2, 1));
        assertTrue(flip.isValid(3,0));
        assertTrue(flip.isValid(4, 1));
    }

    /**
     * Help to print out both the original board and the flipped board view
     */
    public void print_helper(){
        BoardView flip = boardView1.flip();
        System.out.println("Origin:\n"+boardView1.toString()+"\n");
        System.out.println("Flipped:\n"+flip.toString());
    }

}

