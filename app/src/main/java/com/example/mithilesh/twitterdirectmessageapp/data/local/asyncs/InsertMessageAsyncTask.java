package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;

import java.util.List;

public class InsertMessageAsyncTask extends AsyncTask<List<Message>, Void, Void> {

    private MessageDao mAsyncTaskDao;

    public InsertMessageAsyncTask(MessageDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(List<Message>... lists) {

        List<Message> messagesList = lists[0];

        synchronized (this) {
            for (Message message : messagesList) {
                try {
                    mAsyncTaskDao.insert(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}