package com.example.mithilesh.twitterdirectmessageapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;

@Database(entities = {Message.class, TwitterUser.class}, version = 1)
public abstract class DbHelper extends RoomDatabase {

    private static DbHelper INSTANCE;

    public static DbHelper getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (DbHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DbHelper.class, "direct_message_db").build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract MessageDao messageDao();

    public abstract TwitterUserDao twitterUserDao();
}
