package com.example.mithilesh.twitterdirectmessageapp.data;


import android.arch.lifecycle.LiveData;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;

import java.util.List;

public interface DataSource {

    void getAllFriendsList(GetAllFriendsListCallBack callBack);

    void sendMessage(Event event, OnMessageSendCallBack callback);

    void getMessages(OnMessageGetCallBack callBack);

    void loadMessageFromRemoteToDb(LoadMessageFromRemoteToDbCallBack callBack);

    void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack);

    void insertIntoDb(List<Message> messages);

    void deleteAllFromDb();

    LiveData<List<Message>> getAllMessagesFromDb();

    LiveData<List<Message>> getAllMessagesByIdFromDb(long userId);

    LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2);


    interface GetAllFriendsListCallBack {
        void success(ResponseFriends firendsList);

        void failed(int errorCode, String errorMessage);
    }

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
}
