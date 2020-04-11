package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class SpaceTest {

    //Space classes used for testing
    private Space sp1;
    private Space sp2;
    private Space sp3;
    private Space sp4;
    private Space sp5;
    private Space sp6;
    private Space sp7;
    private Space sp8;

    private Piece singleRedPiece = new Piece(Type.SINGLE, Color.RED);
    private Piece singleWhitePiece = new Piece(Type.SINGLE, Color.WHITE);
    private Piece kingRedPiece = new Piece(Type.KING, Color.RED);
    private Piece kingWhitePiece = new Piece(Type.KING, Color.WHITE);


    /**
     * Setting up Space instances, some with pieces, others without to be used
     * in various tests.
     */
    @BeforeEach
    public void setup() {
        // Non black
        sp1 = new Space(0, null, 0);
        // Black
        sp2 = new Space(0, null, 1);
        // black
        sp3 = new Space(1, null, 0);
        // not Black
        sp4 = new Space(1, null, 1);
        sp5 = new Space(0, singleRedPiece, 0);
        sp6 = new Space(0, kingRedPiece, 1);
        sp7 = new Space(1, singleWhitePiece, 0);
        sp8 = new Space(1, kingWhitePiece, 1);
    }

    /**
     * Tests the function that checks whether a space is black and
     *     contains no piece
     */
    @Test
    public void isValidTest() {
        assertFalse(sp1.isValid());
        assertTrue(sp2.isValid());
        assertTrue(sp3.isValid());
        assertFalse(sp4.isValid());
        assertFalse(sp5.isValid());
        assertFalse(sp6.isValid());
        assertFalse(sp7.isValid());
        assertFalse(sp8.isValid());
    }

    /**
     * Tests to make sure the getPiece function returns accurately.
     */
    @Test
    public void getPieceTest() {
        assertNull(sp1.getPiece());
        assertNull(sp2.getPiece());
        assertNull(sp3.getPiece());
        assertNull(sp4.getPiece());
        assertEquals(singleRedPiece, sp5.getPiece());
        assertEquals(kingRedPiece, sp6.getPiece());
        assertEquals(singleWhitePiece, sp7.getPiece());
        assertEquals(kingWhitePiece, sp8.getPiece());
    }

    /**
     * Tests to make sure that a piece can be placed if it is a valid move.
     */
    @Test
    public void setPieceTest() {
        sp1.setPiece(singleRedPiece);
        assertNull(sp1.getPiece());
        sp2.setPiece(singleWhitePiece);
        assertNotEquals(singleRedPiece, sp2.getPiece());
        assertEquals(singleWhitePiece, sp2.getPiece());
        sp2.setPiece(singleRedPiece);
        assertEquals(singleWhitePiece, sp2.getPiece());
        assertNotEquals(singleRedPiece, sp2.getPiece());
        sp5.setPiece(null);
        assertNotNull(sp5.getPiece());
        sp5.removePiece();
        assertNull(sp5.getPiece());
    }

    /**
     * Tests to make sure that the getCellIdx function returns the correct
     * cellIdx for a given Space.
     */
    @Test
    public void getCellIdxTest() {
        assertEquals(0, sp1.getCellIdx());
        assertEquals(0, sp2.getCellIdx());
        assertEquals(1, sp3.getCellIdx());
        assertEquals(1, sp4.getCellIdx());
        assertEquals(0, sp5.getCellIdx());
        assertEquals(0, sp6.getCellIdx());
        assertNotEquals(0, sp7.getCellIdx());
        assertNotEquals(0, sp8.getCellIdx());
    }

    /**
     * Test to make sure that toString works for cases where there is no piece
     */
    @Test
    public void toStringforNoPieceTest(){
        assertEquals(" ", sp1.toString());
    }

}

