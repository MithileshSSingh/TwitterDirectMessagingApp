package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;

import java.util.List;

public class ChatPresenter implements ChatContract.Presenter {


    private Repository mRepository = null;
    private ChatContract.View mView = null;

    private ChatPresenter() {
    }

    public ChatPresenter(Repository repository, ChatContract.View view) {

        mRepository = repository;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void sendMessage(Event event, final ChatContract.View.OnMessageSendCallBack callback) {
        mRepository.sendMessage(event, new DataSource.OnMessageSendCallBack() {
            @Override
            public void success(ResponseSendMessage responseSendMessage) {
                callback.success(responseSendMessage);
            }

            @Override
            public void failed(int errorCode, String errorMsg) {
                callback.failed(errorCode, errorMsg);
            }
        });
    }

    @Override
    public void loadMessageFromRemoteToDb(final ChatContract.View.LoadMessageFromRemoteToDbCallBack callBack) {
        mRepository.loadMessageFromRemoteToDb(new DataSource.LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success(List<Event> eventList) {
                callBack.success();
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }
}
