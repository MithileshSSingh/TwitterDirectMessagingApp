package com.example.mithilesh.twitterdirectmessageapp.data.remote;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;


/**
 * Created by mithilesh on 8/22/16.
 */
public class ApiClient extends TwitterApiClient {

    public ApiClient(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    public ApiClient(TwitterSession session, OkHttpClient okHttpClient) {
        super(session, okHttpClient);
    }

    /**
     * Provide APICalls with defined endpoints
     */
    public APICalls getAPICalls() {
        return getService(APICalls.class);
    }

}
