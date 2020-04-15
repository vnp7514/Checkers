package com.webcheckers.util;

public class Position {
    private int row;
    private int cell;

    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }
    public int getRow() {
        return this.row;
    }

    public int getCell() {
        return this.cell;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)){
            return false;
        }
        Position o = (Position) obj;
        return o.getCell()==this.getCell() && o.getRow()==this.getRow();

    }
}
