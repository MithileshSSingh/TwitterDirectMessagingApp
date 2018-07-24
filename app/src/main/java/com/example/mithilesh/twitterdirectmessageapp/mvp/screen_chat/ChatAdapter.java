package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_chat;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseViewHolder;
import com.example.mithilesh.twitterdirectmessageapp.mvp.listeners.OnItemClickListener;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Event> mListData;

    private OnItemClickListener mListener;

    public ChatAdapter(Context context, ArrayList<Event> listPosts, OnItemClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListData = listPosts;
        mListener = listener;

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(mContext, convertView, mListener);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Event data = mListData.get(position);
        holder.apply(data, position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setListData(ArrayList<Event> data) {
        mListData = data;
        notifyDataSetChanged();
    }
}


class ChatViewHolder extends RecyclerView.ViewHolder implements BaseViewHolder<Event>, View.OnClickListener {

    private View mView;
    private int mPosition;
    private Context mContext;
    private Event mData;

    private TextView tvTitle;
    private ImageView ivSent;
    private ImageView ivNotSent;
    private LinearLayout llMessage;
    private RelativeLayout rootLayout;
    private OnItemClickListener mListener;

    public ChatViewHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mView = itemView;
        mContext = context;
        mListener = listener;

        init();
    }

    public ChatViewHolder(View itemView) {
        super(itemView);
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {

        ivSent = (ImageView) mView.findViewById(R.id.ivSent);
        tvTitle = (TextView) mView.findViewById(R.id.tvMessage);
        ivNotSent = (ImageView) mView.findViewById(R.id.ivNotSent);
        llMessage = (LinearLayout) mView.findViewById(R.id.llMessage);
        rootLayout = (RelativeLayout) mView.findViewById(R.id.rootLayout);

    }

    private void initListener() {
        tvTitle.setOnClickListener(this);
    }

    @Override
    public void apply(Event data, int position) {
        mData = data;
        mPosition = position;

        tvTitle.setText(data.getMessageCreate().getMessageData().getText());

        long myId = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();

        if (myId == Long.valueOf(mData.getMessageCreate().getTarget().getRecipientId())) {
            llMessage.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.background_light));
            rootLayout.setGravity(Gravity.LEFT);
            ivSent.setVisibility(View.GONE);
            ivNotSent.setVisibility(View.GONE);

        } else {

            llMessage.setBackgroundColor(ContextCompat.getColor(mContext, R.color.chat_background));
            rootLayout.setGravity(Gravity.RIGHT);

            if (mData.isSent()) {
                ivSent.setVisibility(View.VISIBLE);
                ivNotSent.setVisibility(View.GONE);
            } else {
                ivSent.setVisibility(View.GONE);
                ivNotSent.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onClick(View view) {
        mListener.onItemClicked(mPosition);
    }
}

