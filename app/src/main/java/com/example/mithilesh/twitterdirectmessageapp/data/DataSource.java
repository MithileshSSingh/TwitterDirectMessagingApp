package com.example.mithilesh.twitterdirectmessageapp.data;


import android.arch.lifecycle.LiveData;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public interface DataSource {

    void sendMessage(Event event, OnMessageSendCallBack callback);

    void getMessages(OnMessageGetCallBack callBack);

    void loadMessageFromRemoteToDb(LoadMessageFromRemoteToDbCallBack callBack);

    void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, LoadUserFromRemoteToDbCallBack callBack);

    void setMessageAsSeen(long myId, long recipientId, CommonCallBack callBack);

    void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack);

    void insertMessageIntoDb(List<Message> messages);

    void deleteAllMessagesFromDb();

    void insertUserIntoDb(List<TwitterUser> twitterUser);

    void updateUserIntoDb(List<TwitterUser> twitterUsers);

    void deleteAllUserFromDb();

    TwitterUser getUserById(long userId);

    LiveData<List<Message>> getAllMessagesFromDb();

    LiveData<List<Message>> getAllMessagesByIdFromDb(long userId);

    LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2);

    LiveData<List<Message>> getAllUnseenMessages(boolean isSeen);

    LiveData<List<TwitterUser>> getAllUsers();

    interface OnMessageGetCallBack {
        void success(ResponseGetMessage responseGetMessage);

        void failed(int errCode, String errorMessage);
    }

    interface OnMessageSendCallBack {
        void success(ResponseSendMessage responseSendMessage);

        void failed(int errorCode, String errorMessage);
    }

    interface LoadMessageFromRemoteToDbCallBack {
        void success(List<Event> eventList);

        void failed(int errorCode, String errorMessage);
    }

    interface SaveMessageToDbCallBack {
        void success();

        void failed(int errorCode, String errorMessage);
    }

    interface CommonCallBack {
        void success();

        void failed(int errorCode, String errorMessage);
    }

    interface LoadUserFromRemoteToDbCallBack {
        void success(ArrayList<User> user);

        void failed(int errorCode, String errorMessage);
    }
}
