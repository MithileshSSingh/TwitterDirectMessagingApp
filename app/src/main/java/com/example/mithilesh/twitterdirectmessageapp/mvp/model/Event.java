package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("created_timestamp")
    @Expose
    private String createdTimeStamp;

    @SerializedName("message_create")
    @Expose
    private MessageCreate messageCreate;

    private boolean isSent = true;

    @SerializedName("is_seen")
    @Expose
    private String isSeen;

    public static Event fromMessageToEvent(Message message) {

        Event event = new Event();

        event.setSent(true);
        event.setType(message.getMessageType());
        event.setCreatedTimeStamp(String.valueOf(message.getCreatedAt()));
        event.setId(String.valueOf(message.getMessageId()));
        event.setIsSeen(String.valueOf(true));
        event.getMessageCreate().getMessageData().setText(message.getMessage());
        event.getMessageCreate().getTarget().setRecipientId(String.valueOf(message.getUserId()));

        return event;
    }

    public Message fromEventToMessage(long myId) {
        Message message = new Message();
        if (Long.valueOf(this.getId()) == myId) {
            message.setMe(true);
        } else {
            message.setMe(false);
        }

        message.setCreatedAt(Long.valueOf(this.getCreatedTimeStamp()));
        message.setMessage(this.getMessageCreate().getMessageData().getText());
        message.setMessageId(Long.valueOf(this.getId()));
        message.setMessageType(this.getType());
        message.setSeen(false);
        message.setUserId(Long.valueOf(this.getMessageCreate().getTarget().getRecipientId()));

        return message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageCreate getMessageCreate() {
        if (messageCreate == null) {
            messageCreate = new MessageCreate();
        }
        return messageCreate;
    }

    public void setMessageCreate(MessageCreate messageCreate) {
        this.messageCreate = messageCreate;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
}
