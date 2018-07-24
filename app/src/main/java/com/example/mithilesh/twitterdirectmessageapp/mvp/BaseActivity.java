package com.example.mithilesh.twitterdirectmessageapp.mvp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list.FriendsListFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_login.LoginActivity;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_login.LoginFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat.ChatFragment;
import com.example.mithilesh.twitterdirectmessageapp.utils.ActivityUtils;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
import com.twitter.sdk.android.core.TwitterCore;

public abstract class BaseActivity extends AppCompatActivity {

    public static String TAG = "TAG";
    public Resources mResources;
    public Activity mActivity;
    public BaseFragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        mResources = this.getResources();
    }

    public void showFragment(int screenId, Bundle bundle) {

        String tag = null;
        mFragment = null;
        boolean addToBackStack = false;

        Intent intent = getIntent();
        Bundle data = null;

        if (intent != null) {
            data = intent.getBundleExtra(AppConstants.IntentKey.EXTRA_DATA);
        }

        switch (screenId) {
            case AppConstants.Screens.SCREEN_LOGIN:

                mFragment = LoginFragment.newInstance();
                tag = LoginFragment.TAG;

                break;
            case AppConstants.Screens.SCREEN_FRIEND_LIST:

                mFragment = FriendsListFragment.newInstance();
                tag = FriendsListFragment.TAG;

                break;
            case AppConstants.Screens.SCREEN_CHAT:

                mFragment = ChatFragment.newInstance();
                tag = ChatFragment.TAG;

                break;
            default:
                break;
        }

        mFragment.setArguments(data);

        if (mFragment != null && tag != null) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content, tag, addToBackStack);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected abstract void init();

    protected abstract void initView();

    protected abstract void initMembers();

    protected abstract void initListeners();

    protected abstract void initData();

    public void logOut() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
