package com.example.mithilesh.twitterdirectmessageapp.data.remote;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.LocalDataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.RequestSendMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

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
    public void getAllFriendsList(final GetAllFriendsListCallBack callBack) {
        Call<ResponseFriends> call = mApiClient.getAPICalls().getAllFriends();

        call.enqueue(new Callback<ResponseFriends>() {
            @Override
            public void success(Result<ResponseFriends> result) {
                callBack.success(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                callBack.failed(exception.getMessage().contains("401") ? 401 : 0, exception.getMessage());
            }
        });
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
    public void insertIntoDb(List<Message> messages) {

    }

    @Override
    public void deleteAllFromDb() {

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
}
