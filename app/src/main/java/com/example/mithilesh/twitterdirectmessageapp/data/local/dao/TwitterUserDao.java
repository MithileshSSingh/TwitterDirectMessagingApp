package com.example.mithilesh.twitterdirectmessageapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;

import java.util.List;

@Dao
public interface TwitterUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TwitterUser... twitterUser);

    @Update
    void updateUser(TwitterUser... twitterUsers);

    @Query("DELETE FROM twitter_user")
    void deleteAll();

    @Query("SELECT * from twitter_user ORDER BY last_updated_at ASC")
    LiveData<List<TwitterUser>> getAllUsers();

    @Query("SELECT * from twitter_user WHERE user_id = :userId")
    TwitterUser getUserById(long userId);

}
