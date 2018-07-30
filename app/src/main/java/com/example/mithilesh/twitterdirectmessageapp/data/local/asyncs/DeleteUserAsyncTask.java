package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;

public class DeleteUserAsyncTask extends AsyncTask<Void, Void, Void> {

    private TwitterUserDao mAsyncTaskDao;

    public DeleteUserAsyncTask(TwitterUserDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        synchronized (this) {
            try {
                mAsyncTaskDao.deleteAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}