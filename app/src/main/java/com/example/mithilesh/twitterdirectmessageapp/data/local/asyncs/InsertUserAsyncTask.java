package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;

import java.util.ArrayList;
import java.util.List;

public class InsertUserAsyncTask extends AsyncTask<List<TwitterUser>, Void, Void> {

    private TwitterUserDao mAsyncTaskDao;

    public InsertUserAsyncTask(TwitterUserDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(List<TwitterUser>... lists) {

        ArrayList<TwitterUser> userList = new ArrayList<>();
        userList.addAll(lists[0]);

        synchronized (this) {

            try {
                mAsyncTaskDao.insert(userList.toArray(new TwitterUser[userList.size()]));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}