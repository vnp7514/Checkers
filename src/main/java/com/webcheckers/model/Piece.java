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

    /**
     * Two pieces are equal if they have the same type and color
     * @param object the object in question
     * @return true if the object is a piece and satisfies the conditions above
     */
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

    /**
     * A String representation of a piece
     * Only these types of piece are defined FOR NOW:
     * White: w
     * Red: r
     * King White: W
     * King Red: R
     * If the piece does not have a defined representation, return ""
     * @return the String representation of a piece
     */
    @Override
    public String toString() {
        if (type == Type.SINGLE){
            if (color == Color.WHITE){
                return "w";
            } else if (color == Color.RED){
                return "r";
            } else {
                return "";
            }
        } else if (type == Type.KING){
            if (color == Color.WHITE){
                return "W";
            } else if (color == Color.RED){
                return "R";
            } else {
                return "";
            }
        }
        return "";
    }

}
