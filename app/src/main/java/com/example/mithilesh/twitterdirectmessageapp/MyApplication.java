package com.example.mithilesh.twitterdirectmessageapp;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setUpTwitter();
        setUpStetho();
        setUpImageLoader();
    }

    private void setUpTwitter() {

        TwitterConfig config = new TwitterConfig.Builder(this).logger(new DefaultLogger(Log.DEBUG)).twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))).debug(true).build();
        Twitter.initialize(config);

    }

    private void setUpStetho() {
        Stetho.initializeWithDefaults(this);

    }

    private void setUpImageLoader() {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
    }
}
