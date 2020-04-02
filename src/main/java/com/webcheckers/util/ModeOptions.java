package com.webcheckers.util;

public class ModeOptions {

    private boolean isGameOver;
    private String gameOverMessage;

    public ModeOptions (boolean over, String message) {
        this.isGameOver = over;
        this.gameOverMessage = message;
    }

    @Override
    public String toString() {
        return "{ " + isGameOver + " : " + " }";
    }
}
