package com.example.mithilesh.twitterdirectmessageapp.data.local.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "message")
public class Message {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
    long messageId;

    @NonNull
    @ColumnInfo(name = "user_id")
    long userId;

    @ColumnInfo(name = "user_name")
    String userName;

    @NonNull
    @ColumnInfo(name = "message")
    String message;

    @NonNull
    @ColumnInfo(name = "type")
    String messageType;

    @NonNull
    @ColumnInfo(name = "is_me")
    boolean isMe;

    @NonNull
    @ColumnInfo(name = "created_at")
    long createdAt;

    @NonNull
    @ColumnInfo(name = "is_seen")
    boolean isSeen;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    @NonNull
    public boolean isMe() {
        return isMe;
    }

    public void setMe(@NonNull boolean me) {
        isMe = me;
    }

    @NonNull
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull long createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(@NonNull boolean seen) {
        isSeen = seen;
    }
}
