package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import com.example.mithilesh.twitterdirectmessageapp.mvp.BasePresenter;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseView;

import java.util.ArrayList;


public class FriendsListContract {

    interface View extends BaseView<Presenter> {

        interface LoadMessageFromRemoteToDbCallBack {
            void success();

            void failed(int errorCode, String errorMessage);
        }

        interface LoadUserDetailsCallBack {
            void success();

            void failed(int errorCode, String errorMessage);
        }
    }

    interface Presenter extends BasePresenter {
        void loadMessageFromRemoteToDb(View.LoadMessageFromRemoteToDbCallBack callBack);

        void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, View.LoadUserDetailsCallBack callBack);
    }
}
