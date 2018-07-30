package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.listeners.OnItemClickListener;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.BeanUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat.ChatActivity;
import com.example.mithilesh.twitterdirectmessageapp.mvp.services.GetDirectMessageJobService;
import com.example.mithilesh.twitterdirectmessageapp.mvp.view_model.MessageViewModel;
import com.example.mithilesh.twitterdirectmessageapp.mvp.view_model.TwitterUserViewModel;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FriendsListFragment extends BaseFragment implements FriendsListContract.View, OnItemClickListener {

    public static final String TAG = FriendsListFragment.class.getSimpleName();

    private FriendsListContract.Presenter mPresenter;

    private RecyclerView rvFriendList;
    private FriendListAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManagerRV;
    private ArrayList<BeanUser> mUserListData = new ArrayList<>();

    private MessageViewModel mMessageViewModel;
    private TwitterUserViewModel mTwitterUserViewModel;

    private HashMap<Long, BeanUser> mUserHashMap = new HashMap<>();

    public FriendsListFragment() {
    }

    public static FriendsListFragment newInstance() {
        return new FriendsListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setScreenTitle(getString(R.string.title_friend_list));
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
        rvFriendList = mView.findViewById(R.id.rvFriendsList);
    }

    @Override
    protected void initMembers() {
        mPresenter = new FriendsListPresenter(RepositoryInjector.provideRepository(getContext()), this);

        initJob();
        initRecyclerView();
    }


    private void initJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mActivity.getApplicationContext()));

        Job myJob = dispatcher.newJobBuilder().setService(GetDirectMessageJobService.class).setTag("twitter-direct-message-unique-tag").setRecurring(true).setLifetime(Lifetime.UNTIL_NEXT_BOOT).setTrigger(Trigger.executionWindow(0, 60)).setReplaceCurrent(false).setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).setConstraints(Constraint.ON_ANY_NETWORK).build();

        dispatcher.mustSchedule(myJob);
    }

    private void initRecyclerView() {

        mAdapter = new FriendListAdapter(mActivity, mUserListData, this);
        mLayoutManagerRV = new LinearLayoutManager(mActivity.getApplicationContext());
        RecyclerView.ItemAnimator itemAnimatorVertical = new DefaultItemAnimator();

        rvFriendList.setHasFixedSize(true);
        rvFriendList.setLayoutManager(mLayoutManagerRV);
        rvFriendList.setItemAnimator(itemAnimatorVertical);
        rvFriendList.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


    }

    private void initTwitterUserViewModel() {
        mTwitterUserViewModel = ViewModelProviders.of(this).get(TwitterUserViewModel.class);
        mTwitterUserViewModel.getmAllUsers().observe(this, new Observer<List<TwitterUser>>() {
            @Override
            public void onChanged(@Nullable List<TwitterUser> twitterUsers) {
                mUserListData.clear();
                ArrayList<BeanUser> beanUserList = new ArrayList<>();
                ArrayList<TwitterUser> userList = new ArrayList<>();
                ArrayList<String> userIdList = new ArrayList<>();

                if (twitterUsers != null && twitterUsers.size() > 0) {
                    userList.addAll(twitterUsers);
                }

                for (TwitterUser user : userList) {
                    BeanUser beanUser = new BeanUser();
                    beanUser.setUser(user);
                    beanUser.setUnReadMessageCount(0);

                    mUserHashMap.put(Long.valueOf(beanUser.getUser().getUserId()), beanUser);

                    if (TextUtils.isEmpty(beanUser.getUser().getProfileImageUrl()) && TextUtils.isEmpty(beanUser.getUser().getUserScreenName()) && TextUtils.isEmpty(beanUser.getUser().getUserName())) {

                        userIdList.add(beanUser.getUser().getUserId());

                    }


                    beanUserList.add(beanUser);
                }

                mUserListData.addAll(beanUserList);
                mAdapter.setListData(mUserListData);
                initMessageViewModel();

                if (userIdList.size() > 0) {
                    loadUserDetail(userIdList);
                }

            }

        });
    }

    private void initMessageViewModel() {
        mMessageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        mMessageViewModel.getAllUnSeenMessages(false).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                if (messages == null || messages.size() == 0 || mUserHashMap.isEmpty()) return;

                for (Message message : messages) {
                    BeanUser beanUser = mUserHashMap.get(message.getSenderId());

                    if (beanUser == null) {
                        continue;
                    }

                    int count = beanUser.getUnReadMessageCount() + 1;
                    beanUser.setUnReadMessageCount(count);
                }

                mAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initData() {
        loadMessages();
    }

    private void loadMessages() {
        mPresenter.loadMessageFromRemoteToDb(new LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success() {
                initTwitterUserViewModel();
                Log.v(TAG, "Messages Loaded successfully");
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                Log.v(TAG,errorMessage);
                if (mActivity != null) {
                    if (errorCode == 401) {
                        mActivity.logOut();
                    } else {
                        initTwitterUserViewModel();
                        Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void loadUserDetail(ArrayList<String> userIdList) {

        mPresenter.loadUserDetailFromRemoteToDb(userIdList, new LoadUserDetailsCallBack() {
            @Override
            public void success() {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                Log.v(TAG,errorMessage);
                if (mActivity != null) {
                    if (errorCode == 401) {
                        mActivity.logOut();
                    } else {
                        Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void setPresenter(FriendsListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClicked(int position) {

        BeanUser user = mUserListData.get(position);
        user.setUnReadMessageCount(0);
        mAdapter.notifyItemChanged(position);
        startChatActivity(user);
    }

    private void startChatActivity(BeanUser user) {

        Intent intent = new Intent(mActivity, ChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentKey.USER, Long.valueOf(user.getUser().getUserId()));
        bundle.putString(AppConstants.IntentKey.USER_NAME, user.getUser().getUserName());
        intent.putExtra(AppConstants.IntentKey.EXTRA_DATA, bundle);

        startActivity(intent);
    }

}
