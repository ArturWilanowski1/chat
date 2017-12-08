package pl.wilanowskiartur.chat.models;

import java.io.Serializable;

public class MessageModel implements Serializable {
    public MessageModel() {

    }

    public enum MessageType {

        MESSAGE(), OEPN_DIALOG();

    }

    private String context;
    private String toWho;
    private MessageType messageType;

    public MessageModel(String context, String toWho, MessageType messageType) {
        this.context = context;
        this.toWho = toWho;
        this.messageType = messageType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
