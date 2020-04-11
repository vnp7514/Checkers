package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class RowTest {

    private Row zeroRow;
    private Row firstRow;
    private Row threeRow;
    private Row sixRow;
    private Row sevenRow;

    private Piece singleRedPiece = new Piece(Type.SINGLE, Color.RED);
    private Piece singleWhitePiece = new Piece(Type.SINGLE, Color.WHITE);
    private Piece kingRedPiece = new Piece(Type.KING, Color.RED);
    private Piece kingWhitePiece = new Piece(Type.KING, Color.WHITE);

    @BeforeEach
    public void setup(){
        zeroRow = new Row(0);
        firstRow = new Row(1);
        threeRow = new Row(3);
        sixRow = new Row(6);
        sevenRow = new Row(7);
    }

    @Test
    public void gettersTest(){
        // getIndex()
        assertEquals(0, zeroRow.getIndex());
        assertEquals(7, sevenRow.getIndex());
        assertNotEquals(7, zeroRow.getIndex());

        // viewPiece()
        assertEquals(singleRedPiece, sevenRow.getSpace(0).getPiece());
        assertEquals(singleWhitePiece, zeroRow.getSpace(1).getPiece());
        assertNull(zeroRow.getSpace(0).getPiece());
    }

    @Test
    public void flipTest(){
        System.out.println("Clarify visually (flip = reverse order): \n");
        System.out.println("original: " + zeroRow.toString());
        System.out.println("flipped:  " + zeroRow.flip().toString());
        System.out.println("original: " + threeRow.toString());
        System.out.println("flipped:  " + threeRow.flip().toString());
        System.out.println("original: " + sixRow.toString());
        System.out.println("flipped:  " + sixRow.flip().toString());
    }

    @Test
    public void modifySpaceTest(){
        System.out.println("Clarify visually: \n");
        System.out.println("original: " + zeroRow.toString());
        System.out.println("original: " + firstRow.toString());
        System.out.println("original: " + sevenRow.toString());
        System.out.println("original: " + threeRow.toString());
        System.out.println("original: " + sixRow.toString());
        System.out.println("Set space 0 in zero Row into a king red piece");
        zeroRow.setSpace(0, kingRedPiece);
        System.out.println(zeroRow.toString());
        System.out.println("Set space 0 in seven Row into a king red piece");
        sevenRow.setSpace(0, kingRedPiece);
        System.out.println(sevenRow.toString());
        System.out.println("Set space 0 in third Row into a king red piece");
        threeRow.setSpace(0, kingRedPiece);
        System.out.println(threeRow.toString());
        System.out.println("Set space 2 in third Row into a king white piece");
        threeRow.setSpace(2, kingWhitePiece);
        System.out.println(threeRow.toString());
        System.out.println("Set space 0 in third Row into null");
        threeRow.setSpace(0, null);
        System.out.println(threeRow.toString());
        System.out.println("Set space 1 in third Row into a single red piece");
        threeRow.setSpace(1, singleRedPiece);
        System.out.println(threeRow.toString());
        System.out.println("Removing piece on space 1 in third Row");
        threeRow.clearSpace(1);
        System.out.println(threeRow.toString());
        System.out.println("Removing piece on space 1 in zero Row");
        zeroRow.clearSpace(1);
        System.out.println(zeroRow.toString());

    }
}

