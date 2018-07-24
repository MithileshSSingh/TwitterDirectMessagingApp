package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;

import java.util.List;


public class FriendsListPresenter implements FriendsListContract.Presenter {

    private Repository mRepository = null;
    private FriendsListContract.View mView = null;

    private FriendsListPresenter() {
    }

    public FriendsListPresenter(Repository repository, FriendsListContract.View view) {

        mRepository = repository;
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void getAllFriendsList(final FriendsListContract.View.GetAllFriendsListCallBack callBack) {
        mRepository.getAllFriendsList(new DataSource.GetAllFriendsListCallBack() {
            @Override
            public void success(ResponseFriends firendsList) {
                callBack.success(firendsList);
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void loadMessageFromRemoteToDb(final FriendsListContract.View.LoadMessageFromRemoteToDbCallBack callBack) {
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
