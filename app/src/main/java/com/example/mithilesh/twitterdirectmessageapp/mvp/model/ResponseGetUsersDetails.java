package com.example.mithilesh.twitterdirectmessageapp.mvp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;

public class ResponseGetUsersDetails {
    @SerializedName("user_list")
    @Expose
    ArrayList<User> userList;

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }
}
