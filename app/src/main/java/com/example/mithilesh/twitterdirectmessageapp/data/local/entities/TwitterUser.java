package com.example.mithilesh.twitterdirectmessageapp.data.local.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "twitter_user")
public class TwitterUser {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    String userId;

    @ColumnInfo(name = "user_screen_name")
    String userScreenName;

    @ColumnInfo(name = "user_name")
    String userName;

    @ColumnInfo(name = "profile_image_url")
    String profileImageUrl;

    @ColumnInfo(name = "last_updated_at")
    String lastUpdatedAt;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public void setUserScreenName(String userScreenName) {
        this.userScreenName = userScreenName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(@NonNull String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
