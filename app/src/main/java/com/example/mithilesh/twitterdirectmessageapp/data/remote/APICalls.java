package com.example.mithilesh.twitterdirectmessageapp.data.remote;

import com.example.mithilesh.twitterdirectmessageapp.mvp.model.RequestSendMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APICalls {
    @GET(ServiceType.ALL_FRIENDS)
    Call<ResponseFriends> getAllFriends();

    @POST(ServiceType.SEND_MESSAGE)
    Call<ResponseSendMessage> sendMessage(@Body RequestSendMessage requestSendMessage);

    @GET(ServiceType.GET_MESSAGES)
    Call<ResponseGetMessage> getMessages();

    class HttpErrorCode {
        public static final Integer NO_CODE = 000;
        public static final Integer LOGIN_FAILED = 401;
    }

    class ServiceType {
        public static final String BASE_URL = "https://api.twitter.com";

        public static final String ALL_FRIENDS = BASE_URL + "/1.1/friends/list.json";
        public static final String SEND_MESSAGE = BASE_URL + "/1.1/direct_messages/events/new.json";
        public static final String GET_MESSAGES = BASE_URL + "/1.1/direct_messages/events/list.json?count=50";
    }
}

