package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.listeners.OnItemClickListener;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.BeanUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;
import com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat.ChatActivity;
import com.example.mithilesh.twitterdirectmessageapp.mvp.services.GetDirectMessageJobService;
import com.example.mithilesh.twitterdirectmessageapp.mvp.view_model.MessageViewModel;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FriendsListFragment extends BaseFragment implements FriendsListContract.View, OnItemClickListener, Observer<List<Message>> {

    public static final String TAG = FriendsListFragment.class.getSimpleName();

    private FriendsListContract.Presenter mPresenter;

    private RecyclerView rvFriendList;
    private FriendListAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManagerRV;
    private ArrayList<BeanUser> mListData = new ArrayList<>();
    private MessageViewModel mMessageViewModel;

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
        initViewModel();
    }


    private void initJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mActivity.getApplicationContext()));

        Job myJob = dispatcher.newJobBuilder().setService(GetDirectMessageJobService.class).setTag("twitter-direct-message-unique-tag").setRecurring(true).setLifetime(Lifetime.UNTIL_NEXT_BOOT).setTrigger(Trigger.executionWindow(0, 60)).setReplaceCurrent(false).setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).setConstraints(Constraint.ON_ANY_NETWORK).build();

        dispatcher.mustSchedule(myJob);
    }

    private void initRecyclerView() {

        mAdapter = new FriendListAdapter(mActivity, mListData, this);
        mLayoutManagerRV = new LinearLayoutManager(mActivity.getApplicationContext());
        RecyclerView.ItemAnimator itemAnimatorVertical = new DefaultItemAnimator();

        rvFriendList.setHasFixedSize(true);
        rvFriendList.setLayoutManager(mLayoutManagerRV);
        rvFriendList.setItemAnimator(itemAnimatorVertical);
        rvFriendList.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


    }

    private void initViewModel() {
        mMessageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        mMessageViewModel.getAllUnSeenMessages(false).observe(this, this);
    }

    @Override
    public void onChanged(@Nullable List<Message> messages) {
        if (messages == null || messages.size() == 0 || mUserHashMap.isEmpty()) return;

        for (Message message : messages) {
            BeanUser beanUser = mUserHashMap.get(message.getUserId());
            int count = beanUser.getUnReadMessageCount()+1;
            beanUser.setUnReadMessageCount(count);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        loadFriendList();
        loadMessages();
    }

    private void loadFriendList() {
        mPresenter.getAllFriendsList(new GetAllFriendsListCallBack() {
            @Override
            public void success(ResponseFriends userListResponse) {
                mListData.clear();
                ArrayList<BeanUser> beanUserList = new ArrayList<>();
                ArrayList<User> userList = new ArrayList<>();

                if (userListResponse.getUser() != null && userListResponse.getUser().size() > 0) {
                    userList.addAll(userListResponse.getUser());
                }

                for (User user : userList) {
                    BeanUser beanUser = new BeanUser();
                    beanUser.setUser(user);
                    beanUser.setUnReadMessageCount(0);

                    mUserHashMap.put(Long.valueOf(beanUser.getUser().idStr), beanUser);

                    beanUserList.add(beanUser);
                }

                mListData.addAll(beanUserList);
                mAdapter.setListData(mListData);
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                if (errorCode == 401) {
                    mActivity.logOut();
                } else {
                    Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadMessages() {
        mPresenter.loadMessageFromRemoteToDb(new LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success() {
                Log.v(TAG, "Messages Loaded successfully");
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                if (errorCode == 401) {
                    mActivity.logOut();
                } else {
                    Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
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

        BeanUser user = mListData.get(position);
        startChatActivity(user);
    }

    private void startChatActivity(BeanUser user) {

        Intent intent = new Intent(mActivity, ChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.IntentKey.USER, user.getUser().getId());
        bundle.putString(AppConstants.IntentKey.USER_NAME, user.getUser().name);
        intent.putExtra(AppConstants.IntentKey.EXTRA_DATA, bundle);

        startActivity(intent);
    }
}
