package com.webcheckers.Checkers;

import com.webcheckers.model.Board;

import java.util.*;

public class BoardView implements Iterable<Row> {

    private Board board;

    public BoardView() {
        this.board = new Board();
    }

    // TODO Stubbed out for now
    @Override
    public Iterator<Row> iterator() {

        return null;
    }

    public Board getBoard(){
        return this.board;
    }

    public void movePiece(){

    }

}
