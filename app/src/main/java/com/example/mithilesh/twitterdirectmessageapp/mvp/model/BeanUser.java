package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

public class BeanUser {
    @SerializedName("un_read_message_count")
    @Expose
    private int unReadMessageCount = 0;
    @SerializedName("user")
    @Expose
    private TwitterUser user;

    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    public void setUnReadMessageCount(int unReadMessageCount) {
        this.unReadMessageCount = unReadMessageCount;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }
}
