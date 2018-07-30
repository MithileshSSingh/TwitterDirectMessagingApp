package com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs;

import android.os.AsyncTask;

import com.example.mithilesh.twitterdirectmessageapp.data.local.dao.TwitterUserDao;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;

import java.util.ArrayList;

public class UpdateUserAsyncTask extends AsyncTask<ArrayList<TwitterUser>, Void, Void> {

    private TwitterUserDao mAsyncTaskDao;

    public UpdateUserAsyncTask(TwitterUserDao dao) {
        mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(ArrayList<TwitterUser>... arrayLists) {

        ArrayList<TwitterUser> listOfUserToUpdate = arrayLists[0];

        synchronized (this) {
            try {

                mAsyncTaskDao.updateUser(listOfUserToUpdate.toArray(new TwitterUser[listOfUserToUpdate.size()]));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
