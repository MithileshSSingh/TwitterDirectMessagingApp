package com.example.mithilesh.twitterdirectmessageapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Message.class}, version = 1)
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

    abstract MessageDao messageDao();

    public void insert(List<Message> messages) {
        if (messages != null && messages.size() > 0) {
            new InsertAsyncTask(this.messageDao()).execute(messages);
        }
    }

    public LiveData<List<Message>> getAllMessages() {
        return this.messageDao().getAllMessages();
    }

    public LiveData<List<Message>> getAllMessagesById(long userId) {
        return this.messageDao().getAllMessagesById(userId);
    }

    public LiveData<List<Message>> getAllMessagesByIds(long userId1, long userId2) {
        return this.messageDao().getAllMessagesByIds(userId1, userId2);
    }

    public LiveData<List<Message>> getAllUnSeenMessages(boolean isSeen) {
        return this.messageDao().getAllUnseenMessages(isSeen);
    }

    public void deleteAll() {
        new DeleteAsyncTask(this.messageDao()).execute();
    }

    public void updateMessagesToSeen(long userId1, long userId2) {
        ArrayList<Long> longArrayList = new ArrayList<>();
        longArrayList.add(userId1);
        longArrayList.add(userId2);

        new UpdateAsyncTask(this.messageDao()).execute(longArrayList);
    }

    private static class UpdateAsyncTask extends AsyncTask<ArrayList<Long>, Void, Void> {

        private MessageDao mAsyncTaskDao;

        UpdateAsyncTask(MessageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ArrayList<Long>... userIds) {

            ArrayList<Long> userIdList = userIds[0];
            long userId1 = userIdList.get(0);
            long userId2 = userIdList.get(1);

            try {
                mAsyncTaskDao.updateMessagesToSeen(userId1, userId2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private MessageDao mAsyncTaskDao;

        DeleteAsyncTask(MessageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                mAsyncTaskDao.deleteAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<List<Message>, Void, Void> {

        private MessageDao mAsyncTaskDao;

        InsertAsyncTask(MessageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<Message>... lists) {

            List<Message> messagesList = lists[0];

            for (Message message : messagesList) {
                try {
                    mAsyncTaskDao.insert(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
