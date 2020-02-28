package com.webcheckers.application;

public class Message {

    // The content of the message
    private String text;

    // The types of message
    public enum MessageType {INFO, ERROR}

    // The types of this message
    private MessageType type;

    /**
     * Constructor for Message class
     * @param text the content of the message
     * @param type the type of the message
     */
    Message(String text, MessageType type){
        this.text = text;
        this.type = type;
    }

    /**
     * Return the text
     * @return the text
     */
    public String getText(){
        return text;
    }

    /**
     * Return the type of message
     * @return the type
     */
    public MessageType getType(){
        return type;
    }
}
