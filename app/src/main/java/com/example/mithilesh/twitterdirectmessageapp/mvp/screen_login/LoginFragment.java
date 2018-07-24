package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list.FriendsListActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class LoginFragment extends BaseFragment implements LoginContract.View {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private LoginContract.Presenter mPresenter;
    private TwitterLoginButton mLoginButton;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginButton != null) {
            mLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setScreenTitle(getString(R.string.title_login));

        if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
            startFriendListActivity();
            return;
        }

        TwitterCore.getInstance().getSessionManager().clearActiveSession();

        init();
    }

    @Override
    protected void init() {
        initView();
        initMembers();
        initListeners();
        initData();
    }

    @Override
    protected void initView() {

        mLoginButton = (TwitterLoginButton) mView.findViewById(R.id.login_button);

    }

    @Override
    protected void initMembers() {
        mPresenter = new LoginPresenter(RepositoryInjector.provideRepository(getContext()), this);

    }

    @Override
    protected void initListeners() {
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                TwitterCore.getInstance().getSessionManager().setActiveSession(session);
                startFriendListActivity();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                exception.printStackTrace();
            }
        });
    }

    private void startFriendListActivity() {
        startActivity(new Intent(mActivity, FriendsListActivity.class));
        mActivity.finish();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
