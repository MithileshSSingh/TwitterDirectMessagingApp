package com.example.mithilesh.twitterdirectmessageapp.mvp.screen_friends_list;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mithilesh.twitterdirectmessageapp.R;
import com.example.mithilesh.twitterdirectmessageapp.mvp.BaseViewHolder;
import com.example.mithilesh.twitterdirectmessageapp.mvp.listeners.OnItemClickListener;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.BeanUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<BeanUser> mListData;

    private OnItemClickListener mListener;

    public FriendListAdapter(Context context, ArrayList<BeanUser> listPosts, OnItemClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListData = listPosts;
        mListener = listener;

    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.item_user, parent, false);
        return new FriendViewHolder(mContext, convertView, mListener);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        BeanUser data = mListData.get(position);
        holder.apply(data, position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setListData(ArrayList<BeanUser> data) {
        mListData = data;
        notifyDataSetChanged();
    }
}


class FriendViewHolder extends RecyclerView.ViewHolder implements BaseViewHolder<BeanUser>, View.OnClickListener {

    private View mView;
    private int mPosition;

    private Context mContext;
    private BeanUser mData;

    private TextView tvName;
    private TextView tvScreenName;

    private ImageView ivProfilePic;

    private ProgressBar progressBar;
    private CardView rootLayout;

    private TextView tvNewMessageCount;

    private OnItemClickListener mListener;

    public FriendViewHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        mView = itemView;
        mContext = context;
        mListener = listener;

        init();
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        tvName = mView.findViewById(R.id.tvName);
        tvScreenName = mView.findViewById(R.id.tvScreenName);

        ivProfilePic = mView.findViewById(R.id.ivProfilePic);
        progressBar = mView.findViewById(R.id.progressBar);
        rootLayout = mView.findViewById(R.id.rootLayout);

        tvNewMessageCount = mView.findViewById(R.id.tvNewMessageCount);

    }

    private void initListener() {
        rootLayout.setOnClickListener(this);
    }


    private void initMember() {
        /**
         * Setting the data here
         */

        String userName = TextUtils.isEmpty(mData.getUser().getUserName()) ? "" : mData.getUser().getUserName();
        String userScreenName = TextUtils.isEmpty(mData.getUser().getUserScreenName()) ? "" : "@" + mData.getUser().getUserScreenName();
        String userProfilePicUrl = TextUtils.isEmpty(mData.getUser().getProfileImageUrl()) ? "" : mData.getUser().getProfileImageUrl();

        tvName.setText(userName);
        tvScreenName.setText(String.valueOf(userScreenName));

        if (mData.getUnReadMessageCount() > 0) {
            tvNewMessageCount.setVisibility(View.VISIBLE);
            tvNewMessageCount.setText(String.valueOf(mData.getUnReadMessageCount()));
        } else {
            tvNewMessageCount.setVisibility(View.GONE);
        }

        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(android.R.drawable.stat_notify_error) // resource or drawable
                .showImageOnFail(android.R.drawable.stat_notify_error).cacheOnDisk(false).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888).build();

        imageLoader.displayImage(userProfilePicUrl, ivProfilePic, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
                ivProfilePic.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.GONE);
                ivProfilePic.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                progressBar.setVisibility(View.GONE);
                ivProfilePic.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
                ivProfilePic.setVisibility(View.VISIBLE);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                progressBar.setVisibility(View.VISIBLE);
                ivProfilePic.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void apply(BeanUser data, int position) {
        mData = data;
        mPosition = position;
        initMember();
    }

    @Override
    public void onClick(View view) {
        mListener.onItemClicked(mPosition);
    }
}

