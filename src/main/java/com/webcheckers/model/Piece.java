package com.webcheckers.model;

public class Piece {

    private Type type;
    private Color color;

    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }


    @Override
    public boolean equals(Object object) {
        if (object instanceof Piece) {
            Piece temp = (Piece) object;
            if (temp.type == this.type && temp.color == this.color) {
                return true;
            }
        }
        return false;
    }

}
