package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

public class ResponseFriends {

    @SerializedName("users")
    @Expose
    private List<User> user;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ResponseFriends{" + "USER=" + user + '}';
    }
}
