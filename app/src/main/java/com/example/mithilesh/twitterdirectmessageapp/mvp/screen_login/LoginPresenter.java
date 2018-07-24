package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_login;

import com.example.mithilesh.twitterdirectmessageapp.data.Repository;


public class LoginPresenter implements LoginContract.Presenter {

    private Repository mRepository = null;
    private LoginContract.View mView = null;

    private LoginPresenter() {
    }

    public LoginPresenter(Repository repository, LoginContract.View view) {

        mRepository = repository;
        mView = view;

        mView.setPresenter(this);
    }

}
