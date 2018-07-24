package com.example.mithilesh.twitterdirectmessageapp.mvp.services;

import android.util.Log;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

public class GetDirectMessageJobService extends JobService {

    private static final String TAG = GetDirectMessageJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {

        Log.v(TAG, "starting");

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        Repository repository = RepositoryInjector.provideRepository(getApplicationContext());
        repository.loadMessageFromRemoteToDb(new DataSource.LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success(List<Event> eventList) {
                Log.v(TAG, "Message loaded and saved to DB");
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                Log.v(TAG, "Error while loading and saving data to db");
            }
        });
        
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.v(TAG, "stopped");

        return false; // Answers the question: "Should this job be retried?"
    }
}