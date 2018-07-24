package com.example.mithilesh.twitterdirectmessageapp.data;


import android.arch.lifecycle.LiveData;

import com.example.mithilesh.twitterdirectmessageapp.data.local.entities.Message;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.Event;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseFriends;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseGetMessage;
import com.example.mithilesh.twitterdirectmessageapp.mvp.model.ResponseSendMessage;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
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
    public void getAllFriendsList(final GetAllFriendsListCallBack callBack) {
        mRemoteDataSource.getAllFriendsList(new GetAllFriendsListCallBack() {
            @Override
            public void success(ResponseFriends firendsList) {
                callBack.success(firendsList);
            }

            @Override
            public void failed(int errorCode, String errorMessage) {
                if (errorCode == 401) {
                    mLocalDataSource.deleteAllFromDb();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
                callBack.failed(errorCode, errorMessage);
            }
        });
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

                mLocalDataSource.insertIntoDb(messageList);

                callback.success(responseSendMessage);
            }

            @Override
            public void failed(int errorCode, String errorMsg) {
                if (errorCode == 401) {
                    mLocalDataSource.deleteAllFromDb();
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
                    mLocalDataSource.deleteAllFromDb();
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
            public void success(List<Event> eventList) {
                if (eventList != null && eventList.size() > 0) {
                    mLocalDataSource.saveMessageToDb(eventList, new SaveMessageToDbCallBack() {
                        @Override
                        public void success() {
                            callBack.success(null);
                        }

                        @Override
                        public void failed(int errorCode, String errorMessage) {
                            if (errorCode == 401) {
                                mLocalDataSource.deleteAllFromDb();
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
                    mLocalDataSource.deleteAllFromDb();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
                callBack.failed(errorCode, errorMessage);
            }
        });

    }

    @Override
    public void saveMessageToDb(List<Event> eventList, SaveMessageToDbCallBack callBack) {

    }

    @Override
    public void insertIntoDb(List<Message> messages) {
        mLocalDataSource.insertIntoDb(messages);
    }


    @Override
    public void deleteAllFromDb() {
        mLocalDataSource.deleteAllFromDb();
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
}