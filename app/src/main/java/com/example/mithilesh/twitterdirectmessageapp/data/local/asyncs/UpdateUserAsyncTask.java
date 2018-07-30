package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.MessageDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;

import java.util.ArrayList;

public class UpdateUserAsyncTask extends AsyncTask<ArrayList<Long>, Void, Void> {

    private TwitterUserDao mAsyncTaskDao;

    public UpdateUserAsyncTask(TwitterUserDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(ArrayList<Long>... userIds) {

        return null;
    }
}
