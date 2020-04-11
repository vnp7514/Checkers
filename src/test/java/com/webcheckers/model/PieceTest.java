package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Tag("Model-tier")
public class PieceTest {

    private Piece singleRedPiece = new Piece(Type.SINGLE, Color.RED);
    private Piece singleWhitePiece = new Piece(Type.SINGLE, Color.WHITE);
    private Piece kingRedPiece = new Piece(Type.KING, Color.RED);
    private Piece kingWhitePiece = new Piece(Type.KING, Color.WHITE);

    @Test
    public void pieceTest(){
        assertEquals(Type.SINGLE, singleRedPiece.getType());
        assertEquals(Type.SINGLE, singleWhitePiece.getType());
        assertNotEquals(Type.SINGLE, kingRedPiece.getType());
        assertEquals(Color.WHITE, singleWhitePiece.getColor());
        assertNotEquals(Color.WHITE, singleRedPiece.getColor());
        assertEquals("w", singleWhitePiece.toString());
        assertEquals("r", singleRedPiece.toString());
        assertEquals("W", kingWhitePiece.toString());
        assertEquals("R", kingRedPiece.toString());
    }
}

