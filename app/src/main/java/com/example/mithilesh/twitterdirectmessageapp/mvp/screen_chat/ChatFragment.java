package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.di.RepositoryInjector;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseFragment;
import com.example.mithilesh.twitterdirectmessageapp.mvp.listeners.OnItemClickListener;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.view_model.MessageViewModel;
import com.example.mithilesh.twitterdirectmessageapp.utils.AppConstants;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends BaseFragment implements ChatContract.View, OnItemClickListener, View.OnClickListener, Observer<List<Message>> {


    public static final String TAG = ChatFragment.class.getSimpleName();

    private String recipientName;
    private long recipientId;
    private long myId;

    private ChatContract.Presenter mPresenter;

    private Button btnSend;
    private ImageButton btnRefresh;

    private EditText etChatText;
    private TextView tvLoading;
    private LinearLayout llChatWindow;

    private RecyclerView rvChatWindow;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManagerRV;

    private ArrayList<Event> mListData = new ArrayList<>();

    private MessageViewModel mMessageViewModel;
    private boolean mIsLoading;

    public ChatFragment() {
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        btnSend = (Button) mView.findViewById(R.id.btnSend);
        etChatText = (EditText) mView.findViewById(R.id.etChatText);
        llChatWindow = (LinearLayout) mView.findViewById(R.id.llChatWindow);
        rvChatWindow = (RecyclerView) mView.findViewById(R.id.rvChatWindow);
        btnRefresh = (ImageButton) mView.findViewById(R.id.btnRefresh);
        tvLoading = (TextView) mView.findViewById(R.id.tvLoading);
    }

    @Override
    protected void initMembers() {
        mPresenter = new ChatPresenter(RepositoryInjector.provideRepository(getActivity()), this);

        Bundle data = getArguments();
        recipientId = data.getLong(AppConstants.IntentKey.USER);
        recipientName = data.getString(AppConstants.IntentKey.USER_NAME);
        myId = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();

        initRecycleView();
        initViewModel();
        setScreenTitle(String.valueOf(recipientName));
    }

    @Override
    protected void initListeners() {
        btnSend.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
    }

    @Override
    protected void initData() {

//        loadMessages();
    }

    private void initRecycleView() {
        mAdapter = new ChatAdapter(mActivity, mListData, this);
        mLayoutManagerRV = new LinearLayoutManager(mActivity.getApplicationContext());

        rvChatWindow.setHasFixedSize(true);
        rvChatWindow.setLayoutManager(mLayoutManagerRV);
        rvChatWindow.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }

    private void loadMessages() {

        loading(true);

        mPresenter.loadMessageFromRemoteToDb(new ChatContract.View.LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success() {
                loading(false);
                Log.v(TAG, "Messages Loaded successfully");
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                loading(false);
                if (errorCode == 401) {
                    mActivity.logOut();
                } else {
                    Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loading(boolean b) {
        mIsLoading = b;
        tvLoading.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void initViewModel() {
        mMessageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        mMessageViewModel.getAllMessagesByIds(myId, recipientId).observe(this, this);
    }

    @Override
    public void onChanged(@Nullable List<Message> messages) {
        ArrayList<Event> eventArrayList = new ArrayList<>();

        if (messages == null || messages.size() == 0) {
            return;
        }

        long lastLocalMessageCreatedTime = 0;

        if (mListData != null && mListData.size() > 0) {
            lastLocalMessageCreatedTime = Long.valueOf(mListData.get(mListData.size() - 1).getCreatedTimeStamp());
        }

        for (Message message : messages) {
            if (lastLocalMessageCreatedTime < message.getCreatedAt()) {

                Event event = Event.fromMessageToEvent(message);
                eventArrayList.add(event);

            }
        }

        if (eventArrayList.size() == 0) {
            return;
        }
        mListData.addAll(eventArrayList);
        mAdapter.setListData(mListData);
        rvChatWindow.smoothScrollToPosition(mListData.size() - 1);

        markMessagesAsSeenOnDb(myId, recipientId);
    }

    private void markMessagesAsSeenOnDb(long myId, long recipientId) {
        mPresenter.setMessageAsSeen(myId, recipientId, new CommonCallBack() {
            @Override
            public void success() {
                Log.v(TAG, "messages updated as read");
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                Log.v(TAG, "error while update : error : " + errorMessage);
            }
        });
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                String message = etChatText.getText().toString();

                if (!TextUtils.isEmpty(message)) {

                    final Event event = new Event();
                    event.setType("message_create");
                    event.getMessageCreate().getTarget().setRecipientId(String.valueOf(recipientId));
                    event.getMessageCreate().getMessageData().setText(message);

                    etChatText.setText("");

                    mPresenter.sendMessage(event, new OnMessageSendCallBack() {
                        @Override
                        public void success(ResponseSendMessage responseSendMessage) {
                            Log.v(TAG, "message sent");
                        }

                        @Override
                        public void failed(int errorCode, String errorMsg) {
                            Log.v(TAG, "failed to send message : error : " + errorMsg);

                            if (errorCode == 401) {
                                mActivity.logOut();
                            } else {
                                mListData.add(event);
                                mListData.get(mListData.size() - 1).setSent(false);
                                mAdapter.notifyDataSetChanged();
                                rvChatWindow.scrollToPosition(mListData.size() - 1);
                            }
                        }
                    });
                }
                break;

            case R.id.btnRefresh:
                if (!mIsLoading) {
                    loadMessages();
                }
                break;
        }
    }

}
