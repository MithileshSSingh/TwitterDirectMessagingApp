package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCreate {

    @SerializedName("target")
    @Expose
    private Target target;

    @SerializedName("sender_id")
    @Expose
    private String senderId;

    @SerializedName("message_data")
    @Expose
    private MessageData messageData;

    public Target getTarget() {
        if (target == null) {
            target = new Target();
        }
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public MessageData getMessageData() {
        if (messageData == null) {
            messageData = new MessageData();
        }
        return messageData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

}
