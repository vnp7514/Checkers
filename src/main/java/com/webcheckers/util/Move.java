package com.webcheckers.util;

public class Move {
    private Position start;
    private Position end;

    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }
    public Position getStart() {
        return this.start;
    }

    public Position getEnd() {
        return this.end;
    }

    public void printString() {
        if (start != null){
            System.out.println("start: " + start.getCell());
            System.out.println("start: " + start.getRow());
        } else {
            System.out.println("null");
        }
        if (end != null){
            System.out.println("end: " + end.getCell());
            System.out.println("end: " + end.getRow());
        } else {
            System.out.println("null");
        }
    }
}
