package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat;

import com.example.mithilesh.twitterdirectmessageapp.mvp.BasePresenter;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseView;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;


public class ChatContract {

    interface View extends BaseView<Presenter> {
        interface OnMessageSendCallBack {
            void success(ResponseSendMessage responseSendMessage);

            void failed(int errorCode, String errorMsg);
        }

        interface LoadMessageFromRemoteToDbCallBack {
            void success();

            void failed(int errorCode, String errorMessage);
        }


        interface CommonCallBack {
            void success();

            void failed(int errorCode, String errorMessage);
        }

    }

    interface Presenter extends BasePresenter {

        void sendMessage(Event event, View.OnMessageSendCallBack callback);

        void loadMessageFromRemoteToDb(View.LoadMessageFromRemoteToDbCallBack callBack);

        void setMessageAsSeen(long myId, long recipientId, View.CommonCallBack callBack);
    }
}
