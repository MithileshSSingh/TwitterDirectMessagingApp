package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;

import java.util.ArrayList;

public class UpdateMessageAsyncTask extends AsyncTask<ArrayList<Long>, Void, Void> {

    private MessageDao mAsyncTaskDao;

    public UpdateMessageAsyncTask(MessageDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(ArrayList<Long>... userIds) {

        ArrayList<Long> userIdList = userIds[0];
        long userId1 = userIdList.get(0);
        long userId2 = userIdList.get(1);

        synchronized (this) {

            try {
                mAsyncTaskDao.updateMessagesToSeen(userId1, userId2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
