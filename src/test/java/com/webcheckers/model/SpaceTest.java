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

    /**
     * Setting up Space instances, some with pieces, others without to be used
     * in various tests.
     */
    @BeforeEach
    public void setup() {
        sp1 = new Space(0, null, 0);
        sp2 = new Space(0, null, 1);
        sp3 = new Space(1, null, 0);
        sp4 = new Space(1, null, 1);
        sp5 = new Space(0, new Piece(Type.SINGLE, Color.RED), 0);
        sp6 = new Space(0, new Piece(Type.KING, Color.RED), 1);
        sp7 = new Space(1, new Piece(Type.SINGLE, Color.WHITE), 0);
        sp8 = new Space(1, new Piece(Type.KING, Color.WHITE), 1);
    }

    /**
     * Tests the validity of a move on a space.
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
        assertEquals(new Piece(Type.SINGLE, Color.RED), sp5.getPiece());
        assertEquals(new Piece(Type.KING, Color.RED), sp6.getPiece());
        assertEquals(new Piece(Type.SINGLE,Color.WHITE), sp7.getPiece());
        assertEquals(new Piece(Type.KING, Color.WHITE), sp8.getPiece());
    }

    /**
     * Tests to make sure that a piece can be placed if it is a valid move.
     */
    @Test
    public void setPieceTest() {
        assertNull(sp1.setPiece(new Piece(Type.KING,Color.WHITE)));
        assertEquals(new Piece(Type.SINGLE, Color.RED), sp2.setPiece(new Piece(Type.SINGLE, Color.RED)));
        assertNotEquals(new Piece(Type.SINGLE,Color.WHITE), sp3.setPiece(new Piece(Type.KING,Color.WHITE)));
        assertNull(sp4.setPiece(new Piece(Type.SINGLE,Color.RED)));
        assertNotEquals(new Piece(Type.KING, Color.RED), sp5.getPiece());
        assertNotEquals(new Piece(Type.KING, Color.WHITE), sp6.getPiece());
        assertNotEquals(new Piece(Type.SINGLE,Color.RED), sp7.getPiece());
        assertNotEquals(new Piece(Type.SINGLE, Color.WHITE), sp8.getPiece());
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

}

