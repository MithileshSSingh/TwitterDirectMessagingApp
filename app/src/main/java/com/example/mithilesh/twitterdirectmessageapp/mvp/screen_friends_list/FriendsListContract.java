package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import com.example.mithilesh.twitterdirectmessageapp.mvp.BasePresenter;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseView;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;


public class FriendsListContract {

    interface View extends BaseView<Presenter> {
        interface GetAllFriendsListCallBack {
            void success(ResponseFriends firendsList);

            void failed(int errorCode, String errorMessage);
        }

        interface LoadMessageFromRemoteToDbCallBack {
            void success();

            void failed(int errorCode, String errorMessage);
        }

    }


    interface Presenter extends BasePresenter {
        void getAllFriendsList(View.GetAllFriendsListCallBack callBack);

        void loadMessageFromRemoteToDb(View.LoadMessageFromRemoteToDbCallBack callBack);
    }
}
