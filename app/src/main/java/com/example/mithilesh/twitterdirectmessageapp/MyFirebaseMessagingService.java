package com.example.mithilesh.twitterdirectmessageapp;

import android.util.Log;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        try {
            if (notification == null || notification.getBody() == null) {
                return;
            }

            JSONObject jsonObject = new JSONObject(notification.getBody());

            String notificationType = jsonObject.getString(AppConstants.Notifications.NOTIFICATION_TYPE_KEY);

            if (notificationType.equals(AppConstants.Notifications.NOTIFICATION_TYPE_NEW_MESSAGE)) {
                Repository repository = RepositoryInjector.provideRepository(getApplicationContext());
                repository.loadMessageFromRemoteToDb(new DataSource.LoadMessageFromRemoteToDbCallBack() {
                    @Override
                    public void success(List<Event> eventList) {
                        Log.v(TAG, "message Loaded successfully");
                    }

                    @Override
                    public void failed(int errorCode, String errorMessage) {
                        if (errorCode == 401) {
                            TwitterCore.getInstance().getSessionManager().clearActiveSession();
                        } else {
                            Log.v(TAG, String.valueOf("failed to load messages : error : " + errorMessage));
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
