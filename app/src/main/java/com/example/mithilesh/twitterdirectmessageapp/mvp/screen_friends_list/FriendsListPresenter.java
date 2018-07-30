package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import com.example.mithilesh.twitterdirectmessageapp.data.DataSource;
import com.example.mithilesh.twitterdirectmessageapp.data.Repository;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.BeanUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
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

    @Override
    public void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, final FriendsListContract.View.LoadUserDetailsCallBack callBack) {
        mRepository.loadUserDetailFromRemoteToDb(listUserIds, new DataSource.LoadUserFromRemoteToDbCallBack() {
            @Override
            public void success(ArrayList<User> user) {
                callBack.success();
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void searchUser(final String userName, final FriendsListContract.View.SearchUserCallBack callBack) {

        mRepository.searchUser(userName, new DataSource.SearchUserCallBack() {
            @Override
            public void success(ArrayList<User> dataList) {
                ArrayList<BeanUser> beanUserArrayList = new ArrayList<>();

                for (User user : dataList) {
                    BeanUser beanUser = new BeanUser();
                    TwitterUser twitterUser = new TwitterUser();

                    twitterUser.setUserId(String.valueOf(user.getId()));
                    twitterUser.setProfileImageUrl(user.profileImageUrlHttps);
                    twitterUser.setUserName(user.name);
                    twitterUser.setUserScreenName(user.screenName);

                    beanUser.setUnReadMessageCount(0);
                    beanUser.setUser(twitterUser);

                    beanUserArrayList.add(beanUser);
                }

                callBack.success(beanUserArrayList);
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }
}
