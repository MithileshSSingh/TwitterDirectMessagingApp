package com.example.mithilesh.twitterdirectmessageapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Message message);

    @Query("DELETE FROM message")
    void deleteAll();

    @Query("SELECT * from message ORDER BY created_at ASC")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * from message WHERE sender_id = :userId ORDER BY created_at ASC")
    LiveData<List<Message>> getAllMessagesById(long userId);

    @Query("SELECT * from message WHERE sender_id = :userId1 AND recipient_id = :userId2  OR sender_id = :userId2 AND recipient_id = :userId1 ORDER BY created_at ASC")
    LiveData<List<Message>> getAllMessagesByIds(long userId1, long userId2);

    @Query("SELECT * from message WHERE is_seen = :isSeen")
    LiveData<List<Message>> getAllUnseenMessages(boolean isSeen);

    @Query("UPDATE message SET is_seen = 'true' where sender_id = :userId1 AND recipient_id = :userId2  OR sender_id = :userId2 AND recipient_id = :userId1")
    void updateMessagesToSeen(long userId1, long userId2);

}
