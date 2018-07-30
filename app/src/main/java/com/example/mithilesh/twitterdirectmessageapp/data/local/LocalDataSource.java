package com.example.mithilesh.twitterdirectmessageapp.data.local;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs.DeleteMessageAsyncTask;
import com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs.InsertMessageAsyncTask;
import com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs.InsertUserAsyncTask;
import com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs.UpdateMessageAsyncTask;
import com.example.mithilesh.twitterdirectmessageapp.data.local.asyncs.UpdateUserAsyncTask;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
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
    public void sendMessage(Event event, OnMessageSendCallBack callback) {

    }

    @Override
    public void getMessages(OnMessageGetCallBack callBack) {

    }

    @Override
    public void loadMessageFromRemoteToDb(LoadMessageFromRemoteToDbCallBack callBack) {

    }

    @Override
    public void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, LoadUserFromRemoteToDbCallBack callBack) {

    }

    @Override
    public void setMessageAsSeen(long myId, long recipientId, CommonCallBack callBack) {
        try {
            ArrayList<Long> longArrayList = new ArrayList<>();
            longArrayList.add(myId);
            longArrayList.add(recipientId);

            new UpdateMessageAsyncTask(mDbHelper.messageDao()).execute(longArrayList);
        } catch (Exception e) {
            callBack.failed(0, e.getMessage());
        }
    }

    @Override
    public void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack) {
        ArrayList<Message> messageList = new ArrayList<>();

        try {
            long myId = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();

            SharedPreferences sp = mContext.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, mContext.MODE_PRIVATE);
            long lastMessageTime = sp.getLong(AppConstants.SHARED_PREFERENCE_LAST_MESSAGE_TIME, 0L);

            for (Event event : eventList) {

                if (Long.valueOf(event.getCreatedTimeStamp()) > lastMessageTime) {
                    Message message = event.fromEventToMessage(myId);
                    messageList.add(message);
                }
            }

            if (messageList.size() > 0) {

                SharedPreferences.Editor editor = sp.edit();
                editor.putLong(AppConstants.SHARED_PREFERENCE_LAST_MESSAGE_TIME, messageList.get(0).getCreatedAt());
                editor.apply();

                insertMessageIntoDb(messageList);

            }

            callBack.success();
        } catch (Exception e) {
            callBack.failed(0, e.getMessage());
        }
    }

    @Override
    public void insertMessageIntoDb(List<Message> messages) {
        if (messages != null && messages.size() > 0) {
            new InsertMessageAsyncTask(mDbHelper.messageDao()).execute(messages);
        }
    }

    @Override
    public void deleteAllMessagesFromDb() {
        new DeleteMessageAsyncTask(mDbHelper.messageDao()).execute();
    }

    @Override
    public void insertUserIntoDb(List<TwitterUser> twitterUsers) {
        if (twitterUsers != null && twitterUsers.size() > 0) {
            new InsertUserAsyncTask(mDbHelper.twitterUserDao()).execute(twitterUsers);
        }
    }

    @Override
    public void updateUserIntoDb(List<TwitterUser> twitterUsers) {

        if (twitterUsers != null && twitterUsers.size() > 0) {
            ArrayList<TwitterUser> twitterUserArrayList = new ArrayList<>();
            twitterUserArrayList.addAll(twitterUsers);
            new UpdateUserAsyncTask(mDbHelper.twitterUserDao()).execute(twitterUserArrayList);
        }

    }

    @Override
    public void deleteAllUserFromDb() {
        mDbHelper.twitterUserDao().deleteAll();
    }

    @Override
    public void searchUser(String userName, SearchUserCallBack callBack) {

    }

    @Override
    public TwitterUser getUserById(long userId) {
        return mDbHelper.twitterUserDao().getUserById(userId);
    }

    @Override
    public LiveData<List<Message>> getAllMessagesFromDb() {
        return mDbHelper.messageDao().getAllMessages();
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdFromDb(long userId) {
        return mDbHelper.messageDao().getAllMessagesById(userId);
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2) {
        return mDbHelper.messageDao().getAllMessagesByIds(userId1, userId2);
    }

    @Override
    public LiveData<List<Message>> getAllUnseenMessages(boolean isSeen) {
        return mDbHelper.messageDao().getAllUnseenMessages(isSeen);
    }

    @Override
    public LiveData<List<TwitterUser>> getAllUsers() {
        return mDbHelper.twitterUserDao().getAllUsers();
    }
}
