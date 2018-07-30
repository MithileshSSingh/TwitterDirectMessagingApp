package com.example.mithilesh.twitterdirectmessageapp.data;


import android.arch.lifecycle.LiveData;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.TwitterUser;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repository implements DataSource {


    private static Repository INSTANCE = null;

    private DataSource mLocalDataSource = null;
    private DataSource mRemoteDataSource = null;

    private Repository() {

    }

    private Repository(DataSource localDataSource, DataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static Repository getInstance(DataSource localDataSource, DataSource remoteDataSource) {

        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(localDataSource, remoteDataSource);
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void sendMessage(final Event event, final OnMessageSendCallBack callback) {
        mRemoteDataSource.sendMessage(event, new OnMessageSendCallBack() {
            @Override
            public void success(ResponseSendMessage responseSendMessage) {

                long myId = TwitterCore.getInstance().getSessionManager().getActiveSession().getUserId();

                Event eventResponse = responseSendMessage.getEvent();
                Message message = eventResponse.fromEventToMessage(myId);

                ArrayList<Message> messageList = new ArrayList<>();
                messageList.add(message);

                mLocalDataSource.insertMessageIntoDb(messageList);

                callback.success(responseSendMessage);
            }

            @Override
            public void failed(int errorCode, String errorMsg) {
                if (errorCode == 401) {
                    mLocalDataSource.deleteAllMessagesFromDb();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }

                callback.failed(errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getMessages(final OnMessageGetCallBack callBack) {
        mRemoteDataSource.getMessages(new OnMessageGetCallBack() {
            @Override
            public void success(ResponseGetMessage responseGetMessage) {
                callBack.success(responseGetMessage);
            }

            @Override
            public void failed(int errCode, String errorMessage) {
                if (errCode == 401) {
                    mLocalDataSource.deleteAllMessagesFromDb();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
                callBack.failed(errCode, errorMessage);
            }
        });
    }

    @Override
    public void loadMessageFromRemoteToDb(final LoadMessageFromRemoteToDbCallBack callBack) {

        mRemoteDataSource.loadMessageFromRemoteToDb(new LoadMessageFromRemoteToDbCallBack() {
            @Override
            public void success(final List<Event> eventList) {
                if (eventList != null && eventList.size() > 0) {

                    mLocalDataSource.saveMessageToDb(eventList, new SaveMessageToDbCallBack() {
                        @Override
                        public void success() {

                            HashMap<String, TwitterUser> userHashMap = new HashMap<>();
                            ArrayList<TwitterUser> listTwitterUser = new ArrayList<>();

                            TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                            String myUserId = String.valueOf(session.getUserId());
                            for (Event event : eventList) {

                                TwitterUser twitterUser = new TwitterUser();


                                String senderId = event.getMessageCreate().getSenderId();
                                String recipientId = event.getMessageCreate().getTarget().getRecipientId();

                                if (!myUserId.equals(senderId)) {
                                    twitterUser.setUserId(senderId);
                                }else if(!myUserId.equals(recipientId)){
                                    twitterUser.setUserId(recipientId);
                                }

                                if (!userHashMap.containsKey(senderId)) {
                                    userHashMap.put(senderId, twitterUser);
                                    listTwitterUser.add(twitterUser);
                                }
                            }

                            mLocalDataSource.insertUserIntoDb(listTwitterUser);

                            callBack.success(null);

                        }

                        @Override
                        public void failed(int errorCode, String errorMessage) {
                            if (errorCode == 401) {
                                mLocalDataSource.deleteAllMessagesFromDb();
                                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                            }
                            callBack.failed(errorCode, errorMessage);
                        }
                    });
                }
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                if (errorCode == 401) {
                    mLocalDataSource.deleteAllMessagesFromDb();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
                callBack.failed(errorCode, errorMessage);
            }
        });

    }

    @Override
    public void loadUserDetailFromRemoteToDb(ArrayList<String> listUserIds, final LoadUserFromRemoteToDbCallBack callBack) {
        mRemoteDataSource.loadUserDetailFromRemoteToDb(listUserIds, new LoadUserFromRemoteToDbCallBack() {
            @Override
            public void success(ArrayList<User> users) {

                ArrayList<TwitterUser> listTwitterUser = new ArrayList<>();

                for (User user : users) {
                    TwitterUser twitterUser = new TwitterUser();

                    twitterUser.setUserId(String.valueOf(user.getId()));
                    twitterUser.setUserName(String.valueOf(user.name));
                    twitterUser.setUserScreenName(String.valueOf(user.screenName));
                    twitterUser.setProfileImageUrl(String.valueOf(user.profileImageUrlHttps));

                    listTwitterUser.add(twitterUser);
                }

                if (listTwitterUser.size() > 0) {
                    mLocalDataSource.updateUserIntoDb(listTwitterUser);
                }

                callBack.success(users);
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void setMessageAsSeen(long myId, long recipientId, final CommonCallBack callBack) {
        mLocalDataSource.setMessageAsSeen(myId, recipientId, new CommonCallBack() {
            @Override
            public void success() {
                callBack.success();
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                callBack.failed(errorCode, errorMessage);
            }
        });
    }

    @Override
    public void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack) {

    }

    @Override
    public void insertMessageIntoDb(List<Message> messages) {
        mLocalDataSource.insertMessageIntoDb(messages);
    }


    @Override
    public void deleteAllMessagesFromDb() {
        mLocalDataSource.deleteAllMessagesFromDb();
    }

    @Override
    public void insertUserIntoDb(List<TwitterUser> twitterUser) {
        mLocalDataSource.insertUserIntoDb(twitterUser);
    }

    @Override
    public void updateUserIntoDb(List<TwitterUser> twitterUsers) {

    }

    @Override
    public void deleteAllUserFromDb() {
        mLocalDataSource.deleteAllUserFromDb();
    }

    @Override
    public TwitterUser getUserById(long userId) {
        return mLocalDataSource.getUserById(userId);
    }

    @Override
    public LiveData<List<Message>> getAllMessagesFromDb() {
        return mLocalDataSource.getAllMessagesFromDb();
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdFromDb(long userId) {
        return mLocalDataSource.getAllMessagesByIdFromDb(userId);
    }

    @Override
    public LiveData<List<Message>> getAllMessagesByIdsFromDb(long userId1, long userId2) {
        return mLocalDataSource.getAllMessagesByIdsFromDb(userId1, userId2);
    }

    @Override
    public LiveData<List<Message>> getAllUnseenMessages(boolean isSeen) {
        return mLocalDataSource.getAllUnseenMessages(isSeen);
    }

    @Override
    public LiveData<List<TwitterUser>> getAllUsers() {
        return mLocalDataSource.getAllUsers();
    }
}