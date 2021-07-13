package com.example.bigapp.data;

public class ChatMessage {
    private String mFromName, mMessage;
    private boolean isSelf;

    public ChatMessage(String fromName, String message, boolean isSelf){
        mMessage = message;
        mFromName = fromName;
        this.isSelf = isSelf;
    }

    public String getFromName() {
        return mFromName;
    }

    public void setFromName(String fromName) {
        mFromName = fromName;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
