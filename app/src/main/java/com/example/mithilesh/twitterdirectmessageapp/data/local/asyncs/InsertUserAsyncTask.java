package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;

import java.util.List;

public class InsertUserAsyncTask extends AsyncTask<List<TwitterUser>, Void, Void> {

    private TwitterUserDao mAsyncTaskDao;

    public InsertUserAsyncTask(TwitterUserDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(List<TwitterUser>... lists) {

        List<TwitterUser> userList = lists[0];

        synchronized (this) {

            for (TwitterUser twitterUser : userList) {
                try {
                    mAsyncTaskDao.insert(twitterUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }
}