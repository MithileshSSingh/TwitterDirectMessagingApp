package com.example.mithilesh.twitterdirectmessageapp.data.remote;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.LocalDataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.RequestSendMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;

public class RemoteDataSource implements DataSource {


    private static RemoteDataSource INSTANCE = null;
    private static ApiClient mApiClient;

    private Context mContext;

    private RemoteDataSource() {

    }

    private RemoteDataSource(Context context) {
        mContext = context;
    }

    public static RemoteDataSource getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource(context);
                }
            }
        }

        initApiClient();

        return INSTANCE;
    }

    private static void initApiClient() {

        TwitterSession activeSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        StethoInterceptor networkInterceptor = new StethoInterceptor();
        OkHttpClient customClient = new OkHttpClient.Builder().addInterceptor(networkInterceptor).build();

        if (activeSession != null) {
            mApiClient = new ApiClient(activeSession, customClient);
            TwitterCore.getInstance().addApiClient(activeSession, mApiClient);
        } else {
            mApiClient = new ApiClient(customClient);
        }
    }

    @Override
    public void sendMessage(Event event, final OnMessageSendCallBack callback) {
        RequestSendMessage requestSendMessage = new RequestSendMessage();
        requestSendMessage.setEvent(event);

        Call<ResponseSendMessage> call = mApiClient.getAPICalls().sendMessage(requestSendMessage);

        call.enqueue(new Callback<ResponseSendMessage>() {
            @Override
            public void success(Result<ResponseSendMessage> result) {
                ResponseSendMessage responseSendMessage = result.data;
                callback.success(responseSendMessage);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callback.failed(exception.getMessage().contains("401") ? 401 : 0, exception.getMessage());
            }
        });
    }

    @Override
    public void getMessages(final OnMessageGetCallBack callBack) {

        Call<ResponseGetMessage> call = mApiClient.getAPICalls().getMessages();

        call.enqueue(new Callback<ResponseGetMessage>() {
            @Override
            public void success(Result<ResponseGetMessage> result) {
                ResponseGetMessage responseGetMessage = result.data;
                callBack.success(responseGetMessage);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callBack.failed(exception.getMessage().contains("401") ? 401 : 0, exception.getMessage());
            }
        });
    }

    @Override
    public void loadMessageFromRemoteToDb(final LoadMessageFromRemoteToDbCallBack callBack) {

        Call<ResponseGetMessage> call = mApiClient.getAPICalls().getMessages();

        call.enqueue(new Callback<ResponseGetMessage>() {
            @Override
            public void success(Result<ResponseGetMessage> result) {
                ResponseGetMessage responseGetMessage = result.data;
                List<Event> eventList = responseGetMessage.getEventList();
                callBack.success(eventList);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callBack.failed(exception.getMessage().contains("401") ? 401 : 0, exception.getMessage());
            }
        });
    }

    @Override
    public void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, final LoadUserFromRemoteToDbCallBack callBack) {

        String userIds = "";
        for (int i = 0; i < listUserIds.size(); i++) {
            userIds = userIds + "," + listUserIds.get(i);
        }

        userIds = userIds.substring(1);

        Call<List<User>> call = mApiClient.getAPICalls().getUsersDetails(userIds);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void success(Result<List<User>> result) {
                if (result != null && result.data != null && result.data.size() > 0) {

                    ArrayList<User> resultList = new ArrayList<>(result.data);

                    callBack.success(resultList);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                callBack.failed(exception.getMessage().contains("401") ? 401 : 0, exception.getMessage());
            }
        });

    }

    @Override
    public void setMessageAsSeen(long myId, long recipientId, CommonCallBack callBack) {

    }

    @Override
    public void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack) {

    }

    @Override
    public void insertMessageIntoDb(List<Message> messages) {

    }

    @Override
    public void deleteAllMessagesFromDb() {

    }

    @Override
    public void insertUserIntoDb(List<TwitterUser> twitterUser) {

    }

    @Override
    public void updateUserIntoDb(List<TwitterUser> twitterUsers) {

    }

    @Override
    public void deleteAllUserFromDb() {

    }

    @Override
    public TwitterUser getUserById(long userId) {
        return null;
    }

    @Override
    public LiveData<List<Message>> getAllMessagesFromDb() {
        return null;
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdFromDb(long userId) {
        return null;
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2) {
        return null;
    }

    @Override
    public LiveData<List<Message>> getAllUnseenMessages(boolean isSeen) {
        return null;
    }

    @Override
    public LiveData<List<TwitterUser>> getAllUsers() {
        return null;
    }
}
