package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCreate {

    @SerializedName("target")
    @Expose
    private Target target;
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

}
