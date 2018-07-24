package com.example.mithilesh.twitterdirectmessageapp.data.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE = null;
    private static DbHelper mDbHelper;

    private Context mContext;

    private LocalDataSource() {

    }

    private LocalDataSource(Context context) {
        mContext = context;
        mDbHelper = DbHelper.getInstance(context);
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(context);
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void getAllFriendsList(GetAllFriendsListCallBack callBack) {

    }

    @Override
    public void sendMessage(Event event, OnMessageSendCallBack callback) {

    }

    @Override
    public void getMessages(OnMessageGetCallBack callBack) {

    }

    @Override
    public void loadMessageFromRemoteToDb(LoadMessageFromRemoteToDbCallBack callBack) {

    }

    @Override
    public void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack) {
        ArrayList<Message> messageList = new ArrayList<>();

        try {
            long myId = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();

            for (Event event : eventList) {

                Message message = event.fromEventToMessage(myId);
                messageList.add(message);
            }

            insertIntoDb(messageList);
            callBack.success();
        } catch (Exception e) {
            callBack.failed(0, e.getMessage());
        }
    }

    @Override
    public void insertIntoDb(List<Message> messages) {
        mDbHelper.insert(messages);
    }

    @Override
    public void deleteAllFromDb() {
        mDbHelper.deleteAll();
    }

    @Override
    public LiveData<List<Message>> getAllMessagesFromDb() {
        return mDbHelper.getAllMessages();
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdFromDb(long userId) {
        return mDbHelper.getAllMessagesById(userId);
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2) {
        return mDbHelper.getAllMessagesByIds(userId1, userId2);
    }
}
