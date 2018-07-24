package com.example.mithilesh.twitterdirectmessageapp.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class CustomProgressBar {
    private static String TAG = CustomProgressBar.class.getSimpleName();

    private static CustomProgressBar INSTANCE;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private CustomProgressBar() {

    }

    private CustomProgressBar(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
    }

    public static CustomProgressBar getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CustomProgressBar(context);
        }
        return INSTANCE;
    }

    public void show(String title, String message) {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
//                mProgressDialog.dismiss();
            }
            Log.v(TAG, "Showing Progress..");
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void show(String message) {

        if (mProgressDialog != null) {
            Log.v(TAG, "Showing Progress..");
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void stop() {
        if (mProgressDialog != null) {
            Log.v(TAG, "Closing Progress..");
            mProgressDialog.dismiss();
            mProgressDialog = null;
            INSTANCE = null;
        }
    }
}
